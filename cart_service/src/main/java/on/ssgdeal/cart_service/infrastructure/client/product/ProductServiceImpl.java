package on.ssgdeal.cart_service.infrastructure.client.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.cart_service.application.service.ProductService;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.ProductFeignClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductFeignClient productFeignClient;
}
