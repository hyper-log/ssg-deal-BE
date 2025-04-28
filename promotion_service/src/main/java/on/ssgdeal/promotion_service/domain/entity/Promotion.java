package on.ssgdeal.promotion_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.promotion_service.domain.entity.dto.CreatePromotionDto;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import on.ssgdeal.promotion_service.domain.vo.PromotionContentImageUrl;
import on.ssgdeal.promotion_service.domain.vo.PromotionPreviewUrl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Embedded
    private PromotionPreviewUrl previewUrl;

    @Embedded
    private PromotionContentImageUrl contentImageUrl;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PromotionStatus status;

    @Column(name = "start_promotion_date", nullable = false)
    private LocalDate startPromotionDate;

    @Column(name = "end_promotion_date", nullable = false)
    private LocalDate endPromotionDate;

    @OneToOne(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Company company;

    public static Promotion create(CreatePromotionDto dto) {

        Company company = Company.create(dto.companyDto());

        Promotion promotion = Promotion.builder()
                .title(dto.title())
                .endPromotionDate(dto.endPromotionDate())
                .startPromotionDate(dto.startPromotionDate())
                .content(dto.content())
                .previewUrl(new PromotionPreviewUrl(dto.previewUrl()))
                .contentImageUrl(new PromotionContentImageUrl(dto.contentImageUrl()))
                .status(dto.status())
                .company(company)
                .build();

        company.assignPromotion(promotion);

        return promotion;
    }
    public void updateToFinishedStatus() {
        this.status = PromotionStatus.FINISHED;
    }
}
