package on.ssgdeal.auth_service.domain.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.auth_service.application.service.dto.SignupAuthRequestDto;
import on.ssgdeal.auth_service.domain.vo.Username;
import on.ssgdeal.auth_service.infrastructure.client.user_service.feign.dto.UserCreateResponse;
import on.ssgdeal.common.auth.enums.AuthRole;
import on.ssgdeal.common.jpa.BaseEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "p_auth")
@Builder(access = AccessLevel.PROTECTED)
public class Auth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    @Embedded
    private Username username;
    private String password;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private AuthRole role;

    public static Auth from(
        SignupAuthRequestDto authRequestDto,
        UserCreateResponse userResponse,
        String encodedPassword
    ) {
        return Auth.builder()
            .userId(userResponse.userId())
            .username(authRequestDto.username())
            .password(encodedPassword)
            .role(authRequestDto.role())
            .build();
    }

}
