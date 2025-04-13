package on.ssgdeal.cart_service.application.service;

import on.ssgdeal.cart_service.application.service.dto.DeleteCartProductRequestDto;

public interface CartService {

    void deleteCartProducts(DeleteCartProductRequestDto requestDto);
}
