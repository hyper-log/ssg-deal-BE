package on.ssgdeal.cart_service.infrastructure.client.product.feign;

import on.ssgdeal.common.presentation.dto.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

    @GetMapping("/internal/v1/promotions/products/{productId}/options/{optionId}/stock")
    CommonResponse<Long> getProductStock(
        @PathVariable Long productId,
        @PathVariable Long optionId
    );

}
