package on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GetProductInfoRequestDto(
    @JsonProperty("getProductDetails") List<GetProductDetails> getProductDetails
) {

    public static GetProductInfoRequestDto from(List<GetProductDetails> products) {
        return new GetProductInfoRequestDto(products);
    }

    public record GetProductDetails(
        Long productId,
        Long optionId,
        Long decreaseStockAmount
    ) {

    }
}