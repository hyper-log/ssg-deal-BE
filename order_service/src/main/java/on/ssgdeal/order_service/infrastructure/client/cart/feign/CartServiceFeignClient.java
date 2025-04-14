package on.ssgdeal.order_service.infrastructure.client.cart.feign;

import on.ssgdeal.order_service.infrastructure.client.cart.feign.dtos.ClearCartRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cart-service")
public interface CartServiceFeignClient {

    @PostMapping("/api/v1/carts/delete")
    void clearCart(@RequestBody ClearCartRequestDto requestDto);

}
