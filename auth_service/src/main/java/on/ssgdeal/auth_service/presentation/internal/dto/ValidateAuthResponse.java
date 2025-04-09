package on.ssgdeal.auth_service.presentation.internal.dto;

public record ValidateAuthResponse(
    String passportId
) {

    public static ValidateAuthResponse of(String passportId) {
        return new ValidateAuthResponse(passportId);
    }
}
