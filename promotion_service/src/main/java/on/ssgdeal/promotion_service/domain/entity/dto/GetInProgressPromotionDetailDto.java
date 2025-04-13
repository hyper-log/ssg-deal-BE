package on.ssgdeal.promotion_service.domain.entity.dto;

import org.springframework.data.domain.Slice;

import java.time.LocalDate;

public record GetInProgressPromotionDetailDto(
        Long promotionId,
        String promotionTitle,
        String contentImageUrl,
        String promotionContent,
        String companyName,
        String companyLogoUrl,
        LocalDate startPromotionDate,
        LocalDate endPromotionDate,
        Slice<ProductDetailDto> promotionProducts
) {
    public record PromotionCompanyDto(
            Long productId,
            String productName,
            String previewUrl,
            Long originalPrice,
            Long promotionPrice
    ) {
    }
    public record ProductDetailDto(
            Long productId,
            String productName,
            String previewUrl,
            Long originalPrice,
            Long promotionPrice
    ) {
    }
}
