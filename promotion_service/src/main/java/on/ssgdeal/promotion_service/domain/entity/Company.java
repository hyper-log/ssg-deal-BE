package on.ssgdeal.promotion_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.promotion_service.domain.entity.dto.CreateCompanyDto;
import on.ssgdeal.promotion_service.domain.vo.CompanyLogoUrl;
import on.ssgdeal.promotion_service.domain.vo.CompanyName;

import java.util.List;

@Entity
@Table(name = "company")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "manager_id", nullable = false)
    private Long managerId;

    @Embedded
    private CompanyName name;

    @Embedded
    private CompanyLogoUrl logoUrl;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Product> products;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    public static Company create(CreateCompanyDto dto) {
        return Company.builder()
                .name(new CompanyName(dto.companyName()))
                .logoUrl(new CompanyLogoUrl(dto.logoUrl()))
                .managerId(dto.managerId())
                .build();
    }

    public void assignPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

}
