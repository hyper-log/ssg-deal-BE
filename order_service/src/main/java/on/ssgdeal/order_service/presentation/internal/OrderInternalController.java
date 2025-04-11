package on.ssgdeal.order_service.presentation.internal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.order_service.application.service.OrderService;
import on.ssgdeal.order_service.application.service.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.application.service.dto.UpdateTotalOrderSuccessRequestDto;
import on.ssgdeal.order_service.presentation.internal.dto.UpdateTotalOrderSuccessRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j(topic = "OrderInternalController")
@RequestMapping("/internal/v1/orders")
public class OrderInternalController {

    private final OrderService orderService;
    private final PassportUtil passportUtil;

    @PostMapping("/payments/success")
    public ResponseEntity<CommonResponse<Void>> createTotalOrderPayment(
        @Valid @RequestBody UpdateTotalOrderSuccessRequest request,
        HttpServletRequest httpServletRequest
    ) {
        final UpdateTotalOrderSuccessRequestDto requestDto = request.toDto();
        Passport passport = passportUtil.getPassportBy(httpServletRequest);
        LoginUserInfoDto loginUserInfo = LoginUserInfoDto.from(passport);
        orderService.createTotalOrderPaymentSuccess(requestDto, loginUserInfo);
        return ResponseEntity.ok(CommonResponse.success());
    }

}
