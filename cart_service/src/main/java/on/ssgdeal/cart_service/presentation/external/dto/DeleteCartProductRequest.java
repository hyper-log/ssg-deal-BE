package on.ssgdeal.cart_service.presentation.external.dto;

import java.util.List;
import on.ssgdeal.cart_service.application.service.dto.DeleteCartProductRequestDto;

public record DeleteCartProductRequest(
    List<ProductOption> productList
) {

    public record ProductOption(
        Long productId,
        Long optionId
    ) {

    }

    public DeleteCartProductRequestDto toDto(Long userId) {
        return new DeleteCartProductRequestDto(
            userId,
            this.productList.stream()
                .map(productOption -> new DeleteCartProductRequestDto.ProductOption(
                    productOption.productId(),
                    productOption.optionId()
                )).toList()
        );
    }
}
