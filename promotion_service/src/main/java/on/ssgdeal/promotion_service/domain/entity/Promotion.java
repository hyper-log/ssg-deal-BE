package on.ssgdeal.promotion_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import on.ssgdeal.promotion_service.domain.vo.PromotionContentImageUrl;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "promotion")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Promotion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "preview", nullable = false)
    private String preview;

    @Embedded
    private PromotionContentImageUrl contentImageUrl;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PromotionStatus status;

    @Column(name = "start_promotion_date", nullable = false)
    private LocalDateTime startPromotionDate;

    @Column(name = "end_promotion_date", nullable = false)
    private LocalDateTime endPromotionDate;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
    private List<Company> companies;


}
