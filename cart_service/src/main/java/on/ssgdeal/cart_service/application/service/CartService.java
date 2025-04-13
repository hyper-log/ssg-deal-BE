package on.ssgdeal.cart_service.application.service;

import on.ssgdeal.cart_service.application.service.dto.UpdateCartProductRequestDto;

public interface CartService {

    void updateCartProduct(UpdateCartProductRequestDto requestDto);
}
