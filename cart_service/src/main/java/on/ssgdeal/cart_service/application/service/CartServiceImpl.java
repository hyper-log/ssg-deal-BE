package on.ssgdeal.cart_service.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.cart_service.application.service.dto.GetProductsByIdsResponseDto;
import on.ssgdeal.cart_service.domain.entity.CartProduct;
import on.ssgdeal.cart_service.domain.repository.CartRepository;
import on.ssgdeal.cart_service.infrastructure.client.product.ProductServiceImpl.GetProductOptionsResponseDto;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductOptionsResponse;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductDetailsResponse;
import on.ssgdeal.cart_service.application.service.dto.AddCartProductRequestDto;
import on.ssgdeal.cart_service.infrastructure.persistence.repository.dto.UpdateCartProductDto;
import on.ssgdeal.cart_service.infrastructure.persistence.generator.RedisKeyGenerator;
import on.ssgdeal.cart_service.infrastructure.persistence.repository.dto.AddCartProductDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "cart-service")
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final ProductService productService;

    @Override
    public GetProductsByIdsResponseDto getCarts(Long userId) {
        log.info("getCarts userId: {}", userId);

        String key = RedisKeyGenerator.generateKey(userId);
        List<CartProduct> cartProducts = cartRepository.findAll(key);
        log.info("getCarts cartProducts: {}", cartProducts);

        List<GetProductDetailsResponse> detailsResponseList =
            productService.getProductsByHashKeys(cartProducts);
        List<GetProductOptionsResponseDto> productOptionsResponses =
            productService.getProductOptions(cartProducts);

        return GetProductsByIdsResponseDto.from(
            detailsResponseList, productOptionsResponses, cartProducts);
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
