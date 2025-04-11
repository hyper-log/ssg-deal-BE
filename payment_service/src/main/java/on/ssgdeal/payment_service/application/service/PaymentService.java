package on.ssgdeal.payment_service.application.service;

import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentRequestDto;
import on.ssgdeal.payment_service.domain.entity.Payment;

public interface PaymentService {

    Payment savePayment(OrderPaymentRequestDto dto);
}
