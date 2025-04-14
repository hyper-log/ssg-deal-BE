package on.ssgdeal.order_service.infrastructure.client.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.order_service.application.service.CartService;
import on.ssgdeal.order_service.infrastructure.client.cart.feign.CartServiceFeignClient;
import on.ssgdeal.order_service.infrastructure.client.cart.feign.dtos.ClearCartRequestDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CartServiceImpl")
public class CartServiceImpl implements CartService {

    private final CartServiceFeignClient feignClient;

    @Override
    public void clearCart(ClearCartRequestDto requestDto) {
        log.info("Clear cart request: {}", requestDto);
        feignClient.clearCart(requestDto);
    }
}
