package on.ssgdeal.promotion_service.infrastructure.persistence.cache.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import on.ssgdeal.promotion_service.domain.entity.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CachingProductDto {

    private Long productId;
    private Long companyId;
    private String name;
    private Long originalPrice;
    private Long promotionPrice;
    private String previewUrl;
    private String contentImgUrl;
    private String content;
    private List<CachingOptionDto> options;
    private Long version;

    public static CachingProductDto from(Product p) {
        List<CachingOptionDto> opts = p.getOptions().stream()
            .map(o -> new CachingOptionDto(
                o.getId(),
                o.getOptionName().getValue(),
                o.getExtraPrice().getValue(),
                o.getProductStock().getValue()))
            .toList();
        return new CachingProductDto(
            p.getId(),
            p.getCompany().getId(),
            p.getName().getValue(),
            p.getOriginalPrice().getValue(),
            p.getPromotionPrice().getValue(),
            p.getPreviewUrl().getValue(),
            p.getContentImgUrl().getValue(),
            p.getContent(),
            opts,
            p.getVersion()
        );
    }
}