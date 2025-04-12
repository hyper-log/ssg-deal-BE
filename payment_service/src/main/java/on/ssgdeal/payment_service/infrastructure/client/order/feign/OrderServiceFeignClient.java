package on.ssgdeal.payment_service.infrastructure.client.order.feign;

import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.payment_service.infrastructure.client.order.feign.dto.GetValidTotalOrderId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderServiceFeignClient {

    @GetMapping("/internal/v1/orders/valid/{totalOrderId}")
    CommonResponse<GetValidTotalOrderId> getValidTotalOrderId(@PathVariable Long totalOrderId);
}
