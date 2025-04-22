package on.ssgdeal.cart_service.application.service;

import static on.ssgdeal.cart_service.exception.CartException.CartProductNotFoundException;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.cart_service.application.service.dto.AddCartProductRequestDto;
import on.ssgdeal.cart_service.application.service.dto.DeleteCartProductRequestDto;
import on.ssgdeal.cart_service.application.service.dto.GetProductsByIdsResponseDto;
import on.ssgdeal.cart_service.application.service.dto.UpdateCartProductRequestDto;
import on.ssgdeal.cart_service.domain.entity.CartProduct;
import on.ssgdeal.cart_service.domain.repository.CartRepository;
import on.ssgdeal.cart_service.infrastructure.client.product.dto.GetProductOptionsResponseDto;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductDetailsResponse;
import on.ssgdeal.cart_service.application.generator.RedisKeyGenerator;
import on.ssgdeal.cart_service.infrastructure.persistence.repository.dto.AddCartProductDto;
import on.ssgdeal.cart_service.infrastructure.persistence.repository.dto.UpdateCartProductDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "cart-service")
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final ProductService productService;

    @Override
    public void updateCartProduct(UpdateCartProductRequestDto requestDto) {
        log.info("update cart product: {}", requestDto);

        String key = RedisKeyGenerator.generateKey(requestDto.userId());
        String hashKey = RedisKeyGenerator.generateHashKey(
            requestDto.productId(), requestDto.optionId());

        cartRepository.findCartProduct(key, hashKey)
            .ifPresentOrElse(
                exist -> {
                    if (requestDto.afterOptionId() != null) {
                        String updateHashKey = RedisKeyGenerator.generateHashKey(
                            requestDto.productId(), requestDto.afterOptionId());
                        CartProduct updateCartProduct;
                        if (requestDto.afterQuantity() != null) {
                            updateCartProduct = CartProduct.create(
                                updateHashKey, requestDto.afterQuantity());
                        } else {
                            updateCartProduct = CartProduct.create(
                                updateHashKey, requestDto.quantity());
                        }
                        cartRepository.deleteCartProducts(key, List.of(exist.getHashKey()));
                        cartRepository.updateCartProduct(
                            UpdateCartProductDto.from(key, updateCartProduct));
                    } else {
                        if (requestDto.afterQuantity() != null) {
                            CartProduct updateCartProduct = CartProduct.create(
                                hashKey, requestDto.afterQuantity());
                            cartRepository.updateCartProduct(
                                UpdateCartProductDto.from(key, updateCartProduct));
                        }
                    }
                },
                () -> {
                    log.warn("cart product not found: {}", requestDto);
                    throw new CartProductNotFoundException();
                }
            );
    }

    @Override
    public void deleteCartProducts(DeleteCartProductRequestDto requestDto) {
        log.info("deleteCartProducts - requestDto: {}", requestDto);

        String key = RedisKeyGenerator.generateKey(requestDto.userId());
        List<String> hashKeys = requestDto.productOptions().stream()
            .map(productOption -> RedisKeyGenerator.generateHashKey(
                productOption.productId(), productOption.optionId()))
            .toList();

        cartRepository.deleteCartProducts(key, hashKeys);
    }

    @Override
    public GetProductsByIdsResponseDto getCarts(Long userId) {
        log.info("getCarts userId: {}", userId);

        String key = RedisKeyGenerator.generateKey(userId);
        List<CartProduct> cartProducts = cartRepository.findAll(key);
        log.info("getCarts cartProducts: {}", cartProducts);

        GetProductDetailsResponse detailsResponse =
            productService.getProductsByHashKeys(cartProducts);
        List<GetProductOptionsResponseDto> productOptionsResponses =
            productService.getProductOptions(cartProducts);

        return GetProductsByIdsResponseDto.from(
            detailsResponse, productOptionsResponses, cartProducts);
    }

    @Override
    public void addCartProduct(AddCartProductRequestDto requestDto) {
        log.info("addCartProduct requestDto: {}", requestDto);

        productService.isProductStockAvailable(requestDto.toDto());

        saveCartProduct(requestDto);
    }

    private void saveCartProduct(AddCartProductRequestDto requestDto) {
        String key = RedisKeyGenerator.generateKey(requestDto.userId());
        String hashKey = RedisKeyGenerator.generateHashKey(
            requestDto.productId(), requestDto.optionId());
        Long hashValue = requestDto.quantity();

        cartRepository.findCartProduct(key, hashKey)
            .ifPresentOrElse(
                exist -> increaseCartProductQuantity(key, exist, requestDto.quantity()),
                () -> addNewCartProduct(key, hashKey, hashValue)
            );
    }

    private void increaseCartProductQuantity(String key, CartProduct exist, Long quantity) {
        log.info("exist product: {}", exist);
        exist.increaseQuantity(quantity);
        var updateCartProductDto = UpdateCartProductDto.from(key, exist);
        cartRepository.updateCartProduct(updateCartProductDto);
    }

    private void addNewCartProduct(String key, String hashKey, Long hashValue) {
        log.info("not exist product");
        var addCartProductDto = AddCartProductDto.from(key, hashKey, hashValue);
        cartRepository.addCartProduct(addCartProductDto);
    }
}
