package on.ssgdeal.promotion_service.infrastructure.persistence.cache.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import on.ssgdeal.promotion_service.domain.entity.Product;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CachingProductDto {

    private Long promotionId;
    private Long companyId;
    private String companyName;
    private Long productId;
    private String productName;
    private Long originalPrice;
    private Long promotionPrice;
    private String previewUrl;
    private String contentImgUrl;
    private String content;
    private List<CachingOptionDto> options;
    private Long version;

    public static CachingProductDto from(Product p) {
        List<CachingOptionDto> opts = p.getOptions().stream()
            .map(o -> CachingOptionDto.builder()
                .optionId(o.getId())
                .optionName(o.getOptionName().getValue())
                .extraPrice(o.getExtraPrice().getValue())
                .productStock(o.getProductStock().getValue())
                .build())
            .toList();
        return CachingProductDto.builder()
            .promotionId(p.getCompany().getPromotion().getId())
            .companyId(p.getCompany().getId())
            .companyName(p.getCompany().getName().getValue())
            .productId(p.getId())
            .productName(p.getName().getValue())
            .originalPrice(p.getOriginalPrice().getValue())
            .promotionPrice(p.getPromotionPrice().getValue())
            .previewUrl(p.getPreviewUrl().getValue())
            .contentImgUrl(p.getContentImgUrl().getValue())
            .content(p.getContent())
            .options(opts)
            .version(p.getVersion())
            .build();
    }
}