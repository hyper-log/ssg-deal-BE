package on.ssgdeal.auth_service.presentation.external.dto;

import lombok.Builder;
import on.ssgdeal.auth_service.application.service.dto.SignupAuthResponseDto;

@Builder
public record SignupAuthResponse(
    String username,
    String nickname
) {

    public static SignupAuthResponse from(SignupAuthResponseDto dto) {
        return new SignupAuthResponse(
            dto.username().toString(),
            dto.nickname()
        );
    }
}
