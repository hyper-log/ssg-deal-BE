package on.ssgdeal.cart_service.application.service;

import on.ssgdeal.cart_service.application.service.dto.AddCartProductRequestDto;

public interface CartService {

    void addCartProduct(AddCartProductRequestDto requestDto);
}
