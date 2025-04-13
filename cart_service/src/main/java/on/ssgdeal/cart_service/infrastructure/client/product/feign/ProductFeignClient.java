package on.ssgdeal.cart_service.infrastructure.client.product.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

}
