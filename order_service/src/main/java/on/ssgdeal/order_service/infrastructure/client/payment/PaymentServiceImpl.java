package on.ssgdeal.order_service.infrastructure.client.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.order_service.application.service.PaymentService;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.PaymentServiceFeignClient;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelOrderPaymentRequestDto;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelOrderPaymentResponseDto;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelTotalOrderPaymentRequestDto;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelTotalOrderPaymentResponseDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PaymentServiceImpl")
public class PaymentServiceImpl implements PaymentService {

    private final PaymentServiceFeignClient feignClient;

    @Override
    public CancelTotalOrderPaymentResponseDto cancelTotalOrderPayment(Long totalOrderId,
        CancelTotalOrderPaymentRequestDto requestDto) {
        log.info("cancelTotalOrderPayment request: {}", totalOrderId);
        return feignClient.cancelTotalOrderPayment(totalOrderId, requestDto).data();
    }

    @Override
    public CancelOrderPaymentResponseDto cancelOrderPayment(Long totalOrderId,
        CancelOrderPaymentRequestDto requestDto) {
        log.info("cancelOrderPayment request: {}", totalOrderId);
        return feignClient.cancelOrderPayment(totalOrderId, requestDto).data();
    }
}