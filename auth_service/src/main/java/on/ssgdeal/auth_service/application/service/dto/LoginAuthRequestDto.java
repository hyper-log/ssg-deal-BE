package on.ssgdeal.auth_service.application.service.dto;


import on.ssgdeal.auth_service.presentation.external.dto.LoginAuthRequest;

public record LoginAuthRequestDto(
    String username,
    String password
) {

    public static LoginAuthRequestDto from(LoginAuthRequest LoginAuthRequest) {
        return new LoginAuthRequestDto(
            LoginAuthRequest.username(),
            LoginAuthRequest.password()
        );
    }
}
