package on.ssgdeal.order_service.application.service;

import on.ssgdeal.order_service.infrastructure.client.cart.feign.dtos.ClearCartRequestDto;

public interface CartService {

    void clearCart(ClearCartRequestDto requestDto);
}
