package on.ssgdeal.auth_service.application.service.dto;

import lombok.Builder;
import on.ssgdeal.auth_service.domain.entity.Auth;
import on.ssgdeal.auth_service.domain.vo.Username;
import on.ssgdeal.auth_service.infrastructure.client.user_service.feign.dto.UserCreateResponse;


@Builder
public record SignupAuthResponseDto(
    Username username,
    String nickname
) {

    public static SignupAuthResponseDto from(Auth auth, UserCreateResponse dto) {
        return new SignupAuthResponseDto(
            auth.getUsername(),
            dto.nickname()
        );
    }
}
