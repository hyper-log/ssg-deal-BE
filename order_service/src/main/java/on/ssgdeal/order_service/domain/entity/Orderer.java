package on.ssgdeal.order_service.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.order_service.application.service.dto.CreateUserInfoDto;
import on.ssgdeal.order_service.domain.vo.DeliveryRequest;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("is_deleted = false")
public class Orderer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "total_order_id")
    private TotalOrder totalOrder;

    private Long userId;
    private String nickname;
    private String slackEmail;
    private String destination;

    private DeliveryRequest deliveryRequest;

    public static Orderer create(TotalOrder totalOrder, CreateUserInfoDto dto) {
        return Orderer.builder()
            .userId(dto.userId())
            .totalOrder(totalOrder)
            .nickname(dto.nickname())
            .slackEmail(dto.slackEmail())
            .destination(dto.destination())
            .deliveryRequest(new DeliveryRequest(dto.deliveryRequest()))
            .build();
    }
}
