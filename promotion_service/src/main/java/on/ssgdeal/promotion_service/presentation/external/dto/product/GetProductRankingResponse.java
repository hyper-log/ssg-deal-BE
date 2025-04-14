package on.ssgdeal.promotion_service.presentation.external.dto.product;

import java.util.List;

public record GetProductRankingResponse(
    List<ProductRanking> productRankings
) {

    public record ProductRanking(
        Long productRanking,
        Long productId,
        String productPreviewImgUrl,
        String productName,
        String companyName,
        Long originalPrice,
        Long promotionPrice
    ) {

    }

}
