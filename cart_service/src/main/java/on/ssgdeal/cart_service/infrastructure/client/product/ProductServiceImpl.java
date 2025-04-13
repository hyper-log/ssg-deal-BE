package on.ssgdeal.cart_service.infrastructure.client.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.cart_service.application.service.ProductService;
import on.ssgdeal.cart_service.exception.CartException.NotEnoughStockException;
import on.ssgdeal.cart_service.infrastructure.client.product.dto.IsProductStockAvailableRequestDto;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.ProductFeignClient;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductFeignClient productFeignClient;

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
}
