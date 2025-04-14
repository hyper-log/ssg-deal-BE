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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.promotion_service.domain.vo.ProductContentImageUrl;
import on.ssgdeal.promotion_service.domain.vo.ProductName;
import on.ssgdeal.promotion_service.domain.vo.ProductOriginalPrice;
import on.ssgdeal.promotion_service.domain.vo.ProductPreviewUrl;
import on.ssgdeal.promotion_service.domain.vo.ProductPromotionPrice;

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
    private List<ProductOption> options = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductRankingInfo> productRankingInfos;

}
