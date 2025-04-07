package on.ssgdeal.promotion_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.promotion_service.domain.vo.*;

import java.util.List;

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductRankingInfo> productRankingInfos;

}
