package on.ssgdeal.payment_service.infrastructure.client.order.feign;

import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.payment_service.infrastructure.client.order.feign.dto.CreateOrderPaymentFailRequestDto;
import on.ssgdeal.payment_service.infrastructure.client.order.feign.dto.CreateOrderPaymentSuccessRequestDto;
import on.ssgdeal.payment_service.infrastructure.client.order.feign.dto.GetValidTotalOrderId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service")
public interface OrderServiceFeignClient {

    @GetMapping("/internal/v1/orders/valid/{totalOrderId}")
    CommonResponse<GetValidTotalOrderId> getValidTotalOrderId(@PathVariable Long totalOrderId);

    @PostMapping("/internal/v1/orders/payments/success")
    CommonResponse<Void> createOrderPaymentSuccess(
        @RequestBody CreateOrderPaymentSuccessRequestDto requestDto);

    @PostMapping("/internal/v1/orders/payments/fail")
    CommonResponse<Void> createOrderPaymentFail(
        @RequestBody CreateOrderPaymentFailRequestDto requestDto);
}
