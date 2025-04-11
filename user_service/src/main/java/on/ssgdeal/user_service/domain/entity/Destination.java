package on.ssgdeal.user_service.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.user_service.application.dto.destination.CreateMyDestinationDto;
import on.ssgdeal.user_service.domain.vo.Address;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("is_deleted = false")
@Table(name = "destination")
@SQLDelete(sql = "UPDATE destination SET is_deleted = true WHERE id = ?")
public class Destination extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;
    private Address address;
    private String name;

    public static Destination create(Passport passport, CreateMyDestinationDto dto) {
        return Destination.builder()
            .userId(passport.getUserId())
            .address(new Address(dto.address()))
            .name(dto.destinationName())
            .build();
    }

}
