package on.ssgdeal.promotion_service.presentation.external.dto.product;

import java.util.List;

public record FindByIdResponse(
    Long productId,
    String productName,
    String companyName,
    String productPreviewImgUrl,
    Long originalPrice,
    Long promotionPrice,
    String productContentUrl,
    String productContent,
    List<Options> options
) {

    public record Options(
        String optionName,
        Long stock,
        Long extraPrice
    ) {

    }

}