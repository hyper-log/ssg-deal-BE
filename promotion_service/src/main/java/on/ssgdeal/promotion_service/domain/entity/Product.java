package on.ssgdeal.promotion_service.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.promotion_service.application.service.dto.product.UpdateProductRequestDto;
import on.ssgdeal.promotion_service.domain.entity.dto.CreateProductDto;
import on.ssgdeal.promotion_service.domain.vo.ProductContentImageUrl;
import on.ssgdeal.promotion_service.domain.vo.ProductName;
import on.ssgdeal.promotion_service.domain.vo.ProductOriginalPrice;
import on.ssgdeal.promotion_service.domain.vo.ProductPreviewUrl;
import on.ssgdeal.promotion_service.domain.vo.ProductPromotionPrice;
import org.springframework.data.annotation.Version;

@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @Embedded
    private ProductName name;
    @Embedded
    private ProductOriginalPrice originalPrice;

    @Embedded
    private ProductPromotionPrice promotionPrice;

    @Embedded
    private ProductPreviewUrl previewUrl;

    @Embedded
    private ProductContentImageUrl contentImgUrl;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    @Builder.Default
    private List<ProductOption> options = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductRankingInfo> productRankingInfos;

    @Version
    @Column(nullable = false)
    private Long version;

    public static Product create(CreateProductDto dto) {
        List<ProductOption> productOptions = dto.options().stream()
                .map(ProductOption::create)
                .toList();

        return Product.builder()
                .company(dto.company())
                .content(dto.content())
                .contentImgUrl(new ProductContentImageUrl(dto.contentImgUrl()))
                .originalPrice(new ProductOriginalPrice(dto.originalPrice()))
                .promotionPrice(new ProductPromotionPrice(dto.promotionPrice()))
                .name(new ProductName(dto.productName()))
                .previewUrl(new ProductPreviewUrl(dto.previewUrl()))
                .options(productOptions)
                .version(0L)
                .build();
    }

    public void update(UpdateProductDto dto) {
        if (dto.productName != null) {
            this.name = new ProductName(dto.productName);
        }
        if (dto.originalPrice != null) {
            this.originalPrice = new ProductOriginalPrice(dto.originalPrice);
        }
        if (dto.promotionPrice != null) {
            this.promotionPrice = new ProductPromotionPrice(dto.promotionPrice);
        }
        if (dto.previewUrl != null) {
            this.previewUrl = new ProductPreviewUrl(dto.previewUrl);
        }
        if (dto.contentImgUrl != null) {
            this.contentImgUrl = new ProductContentImageUrl(dto.contentImgUrl);
        }
        if (dto.content != null) {
            this.content = dto.content;
        }
    }

    @Builder
    public record UpdateProductDto(
        String productName,
        Long originalPrice,
        Long promotionPrice,
        String previewUrl,
        String contentImgUrl,
        String content
    ) {

        public static UpdateProductDto from(
            UpdateProductRequestDto dto
        ) {
            return UpdateProductDto.builder()
                .productName(dto.productName())
                .originalPrice(dto.originalPrice())
                .promotionPrice(dto.promotionPrice())
                .previewUrl(dto.previewUrl())
                .contentImgUrl(dto.contentImgUrl())
                .content(dto.content())
                .build();
        }
    }
}
