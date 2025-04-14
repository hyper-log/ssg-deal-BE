package on.ssgdeal.cart_service.application.service;

import on.ssgdeal.cart_service.infrastructure.client.product.dto.IsProductStockAvailableRequestDto;

public interface ProductService {

    void isProductStockAvailable(IsProductStockAvailableRequestDto requestDto);
}
