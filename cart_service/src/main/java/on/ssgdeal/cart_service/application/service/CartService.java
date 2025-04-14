package on.ssgdeal.cart_service.application.service;

import on.ssgdeal.cart_service.application.service.dto.GetProductsByIdsResponseDto;
import on.ssgdeal.cart_service.application.service.dto.AddCartProductRequestDto;

public interface CartService {

    GetProductsByIdsResponseDto getCarts(Long userId);

    void addCartProduct(AddCartProductRequestDto requestDto);
}
