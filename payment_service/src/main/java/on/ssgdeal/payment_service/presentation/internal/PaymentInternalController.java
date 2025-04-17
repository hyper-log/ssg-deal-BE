package on.ssgdeal.payment_service.presentation.internal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.payment_service.application.service.PaymentProcessorService;
import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentPartialCancelRequestDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentCancelResponseDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentPartialCancelResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1/payments")
public class PaymentInternalController {

    private final PaymentProcessorService paymentProcessorService;

    @PutMapping("/{totalOrderId}/cancel")
    public ResponseEntity<CommonResponse<OrderPaymentCancelResponseDto>> orderCancelPayment(
        @PathVariable Long totalOrderId
    ) {
        final var responseDto = paymentProcessorService.orderPaymentCancel(totalOrderId);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    @PutMapping("/{totalOrderId}/partial-cancel")
    public ResponseEntity<CommonResponse<OrderPaymentPartialCancelResponseDto>> orderPartialCancelPayment(
        @PathVariable Long totalOrderId,
        @RequestBody @Valid OrderPaymentPartialCancelRequestDto partialCancelRequestDto
    ) {
        final var responseDto = paymentProcessorService.orderPaymentPartialCancel(totalOrderId,
            partialCancelRequestDto);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }
}
