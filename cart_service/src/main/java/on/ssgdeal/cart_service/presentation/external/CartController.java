package on.ssgdeal.cart_service.presentation.external;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.cart_service.application.service.CartService;
import on.ssgdeal.cart_service.application.service.dto.DeleteCartProductRequestDto;
import on.ssgdeal.cart_service.presentation.external.dto.DeleteCartProductRequest;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private final PassportUtil passportUtil;

    @PostMapping("/delete")
    public ResponseEntity<CommonResponse<Void>> deleteCartProducts(
        @RequestBody DeleteCartProductRequest request,
        HttpServletRequest servletRequest
    ) {
        Passport passport = passportUtil.getPassportBy(servletRequest);
        DeleteCartProductRequestDto requestDto = request.toDto(passport.getUserId());
        cartService.deleteCartProducts(requestDto);
        return ResponseEntity.ok(CommonResponse.success());
    }
}