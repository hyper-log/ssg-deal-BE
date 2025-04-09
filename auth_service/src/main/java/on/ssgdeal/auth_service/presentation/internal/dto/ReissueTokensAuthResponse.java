package on.ssgdeal.auth_service.presentation.internal.dto;

public record ReissueTokensAuthResponse(
    String passportId
) {

    public static ReissueTokensAuthResponse from(String passportId) {
        return new ReissueTokensAuthResponse(
            passportId
        );
    }
}
