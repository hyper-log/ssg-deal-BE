package on.ssgdeal.order_service.infrastructure.client.payment.feign;

import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelOrderPaymentRequestDto;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelOrderPaymentResponseDto;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelTotalOrderPaymentRequestDto;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelTotalOrderPaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentServiceFeignClient {

    @PutMapping("/internal/v1/payments/{totalOrderId}/cancel")
    CommonResponse<CancelTotalOrderPaymentResponseDto> cancelTotalOrderPayment
        (@PathVariable("totalOrderId") Long totalOrderId,
            @RequestBody CancelTotalOrderPaymentRequestDto requestDto);

    @PutMapping("/internal/v1/payments/{totalOrderId}/partial-cancel")
    CommonResponse<CancelOrderPaymentResponseDto> cancelOrderPayment(
        @PathVariable("totalOrderId") Long totalOrderId,
        @RequestBody CancelOrderPaymentRequestDto requestDto);

}
