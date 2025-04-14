package on.ssgdeal.payment_service.application.service;

import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentRequestDto;
import on.ssgdeal.payment_service.application.service.dto.response.GetPaymentsResponseDto;
import on.ssgdeal.payment_service.domain.entity.Payment;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    Payment savePayment(OrderPaymentRequestDto dto);

    Payment getPaymentByTotalOrderId(Long totalOrderId);

    PageDto<GetPaymentsResponseDto> getPayments(Long totalOrderId, Pageable pageable);
}
