package on.ssgdeal.cart_service.application.service;

import java.util.List;
import on.ssgdeal.cart_service.domain.entity.CartProduct;
import on.ssgdeal.cart_service.infrastructure.client.product.dto.GetProductOptionsResponseDto;
import on.ssgdeal.cart_service.infrastructure.client.product.dto.IsProductStockAvailableRequestDto;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductDetailsResponse;

public interface ProductService {

    GetProductDetailsResponse getProductsByHashKeys(List<CartProduct> cartProducts);

    List<GetProductOptionsResponseDto> getProductOptions(List<CartProduct> cartProducts);

    void isProductStockAvailable(IsProductStockAvailableRequestDto requestDto);
}
