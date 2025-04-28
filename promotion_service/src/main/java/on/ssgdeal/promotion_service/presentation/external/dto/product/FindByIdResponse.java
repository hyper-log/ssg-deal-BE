package on.ssgdeal.promotion_service.presentation.external.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FindByIdResponse(
    Long productId,
    String productName,
    String companyName,
    @JsonProperty("previewUrl")
    String productPreviewImgUrl,
    Long originalPrice,
    Long promotionPrice,
    @JsonProperty("contentImgUrl")
    String productContentUrl,
    @JsonProperty("content")
    String productContent,
    @JsonProperty("options")
    List<Options> options
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Options(
        String optionName,
        @JsonProperty("productStock")
        Long stock,
        Long extraPrice
    ) {

    }

}