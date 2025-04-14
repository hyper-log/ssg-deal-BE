package on.ssgdeal.cart_service.infrastructure.client.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.cart_service.application.service.ProductService;
import on.ssgdeal.cart_service.domain.entity.CartProduct;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.ProductFeignClient;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductDetailsRequest;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductDetailsRequest.ProductDetail;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductDetailsResponse;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductOptionsResponse;
import on.ssgdeal.cart_service.infrastructure.persistence.generator.RedisKeyGenerator;
import on.ssgdeal.cart_service.exception.CartException.NotEnoughStockException;
import on.ssgdeal.cart_service.infrastructure.client.product.dto.IsProductStockAvailableRequestDto;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductFeignClient productFeignClient;

    @Override
    public List<GetProductDetailsResponse> getProductsByHashKeys(List<CartProduct> cartProducts) {
        log.info("getProductsByIds cartProducts: {}", cartProducts);

        return cartProducts.stream()
            .map(key -> {
                GetProductDetailsRequest request = new GetProductDetailsRequest(
                    List.of(
                        new ProductDetail(
                            RedisKeyGenerator.parseProductId(key.getHashKey()),
                            RedisKeyGenerator.parseOptionId(key.getHashKey())
                        )
                    )
                );
                return productFeignClient.getProductDetails(request);
            })
            .toList();
    }

    @Override
    public List<GetProductOptionsResponseDto> getProductOptions(List<CartProduct> cartProducts) {
        log.info("getProductOptions cartProducts: {}", cartProducts);
        return cartProducts.stream()
            .map(key -> {
                Long productId = RedisKeyGenerator.parseProductId(key.getHashKey());
                GetProductOptionsResponse response =
                    productFeignClient.getProductOptions(productId);
                return new GetProductOptionsResponseDto(productId, response.options().stream()
                    .map(option -> new GetProductOptionsResponseDto.Option(
                        option.optionId(),
                        option.optionName(),
                        option.extraPrice(),
                        option.productStock()
                    )).toList());
            })
            .toList();
    }

    @Override
    public void isProductStockAvailable(IsProductStockAvailableRequestDto request) {
        log.info("isProductStockAvailable request: {}", request);

        CommonResponse<Long> response = productFeignClient.getProductStock(
            request.productId(), request.optionId());
        Long stock = response.data();
        if (stock == null || stock < request.quantity()) {
            throw new NotEnoughStockException();
        }
    }

    public record GetProductOptionsResponseDto(
        Long productId,
        List<Option> options
    ) {

        public record Option(
            Long optionId,
            String optionName,
            Long extraPrice,
            Long productStock
        ) {

        }
    }
}
