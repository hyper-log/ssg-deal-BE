package on.ssgdeal.payment_service.presentation.external;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.payment_service.application.service.PaymentProcessorService;
import on.ssgdeal.payment_service.application.service.PaymentService;
import on.ssgdeal.payment_service.application.service.dto.response.GetPaymentsResponseDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentResponseDto;
import on.ssgdeal.payment_service.presentation.external.dto.OrderPaymentRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentProcessorService paymentProcessorService;
    private final PaymentService paymentService;
    private final PassportUtil passportUtil;

    @PostMapping("/confirm")
    public ResponseEntity<CommonResponse<OrderPaymentResponseDto>> orderPayment(
        @RequestBody @Valid OrderPaymentRequest request,
        HttpServletRequest servletRequest
    ) {
        final Passport passport = passportUtil.getPassportBy(servletRequest);
        final var requestDto = request.toDto(passport.getUserId());
        final var responseDto = paymentProcessorService.orderPayment(requestDto);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    @GetMapping("/{totalOrderId}")
    public ResponseEntity<CommonResponse<PageDto<GetPaymentsResponseDto>>> getPayments(
        @PathVariable Long totalOrderId,
        @PageableDefault Pageable pageable
    ) {
        final var responseDto = paymentService.getPayments(totalOrderId, pageable);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }
}
