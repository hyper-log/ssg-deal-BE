package on.ssgdeal.cart_service.presentation.external;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.cart_service.application.service.CartService;
import on.ssgdeal.cart_service.application.service.dto.UpdateCartProductRequestDto;
import on.ssgdeal.cart_service.presentation.external.dto.UpdateCartProductRequest;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private final PassportUtil passportUtil;

    @PatchMapping
    public ResponseEntity<CommonResponse<Void>> updateCartProduct(
        @Valid @RequestBody UpdateCartProductRequest request,
        HttpServletRequest servletRequest
    ) {
        Passport passport = passportUtil.getPassportBy(servletRequest);
        UpdateCartProductRequestDto requestDto = request.toDto(passport.getUserId());
        cartService.updateCartProduct(requestDto);
        return ResponseEntity.ok(CommonResponse.success());
    }

}
