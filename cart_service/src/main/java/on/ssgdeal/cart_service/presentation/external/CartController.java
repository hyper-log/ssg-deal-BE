package on.ssgdeal.cart_service.presentation.external;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.cart_service.application.service.CartService;
import on.ssgdeal.cart_service.application.service.dto.GetProductsByIdsResponseDto;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.cart_service.application.service.CartService;
import on.ssgdeal.cart_service.presentation.external.dto.AddCartProductRequest;
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

    @GetMapping
    public ResponseEntity<CommonResponse<GetProductsByIdsResponseDto>> getCarts(
        HttpServletRequest servletRequest
    ) {
        Passport passport = passportUtil.getPassportBy(servletRequest);
        GetProductsByIdsResponseDto requestDto = cartService.getCarts(passport.getUserId());
        return ResponseEntity.ok(CommonResponse.success(requestDto));

    @PostMapping
    public ResponseEntity<CommonResponse<Void>> addCartProduct(
        @RequestBody @Valid final AddCartProductRequest request,
        final HttpServletRequest servletRequest
    ) {
        final Long userId = passportUtil.getPassportBy(servletRequest).getUserId();
        final var requestDto = request.toDto(userId);
        cartService.addCartProduct(requestDto);
        return ResponseEntity.ok(CommonResponse.success());
    }
}
