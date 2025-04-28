package on.ssgdeal.promotion_service.presentation.external.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FindByPromotionIdResponse(
    Long productId,
    String productName,
    String companyName,
    @JsonProperty("previewUrl")
    String productPreviewImgUrl,
    Long originalPrice,
    Long promotionPrice
) {

}
