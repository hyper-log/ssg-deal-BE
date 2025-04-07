package on.ssgdeal.promotion_service.domain.entity;
import jakarta.persistence.*;
import lombok.*;
import on.ssgdeal.common.jpa.BaseEntity;

@Entity
@Table(name = "product_ranking_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class ProductRankingInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_ranking_id", nullable = false)
    private ProductRanking productRanking;

    @Column(name = "score", nullable = false)
    private Long score;

}
