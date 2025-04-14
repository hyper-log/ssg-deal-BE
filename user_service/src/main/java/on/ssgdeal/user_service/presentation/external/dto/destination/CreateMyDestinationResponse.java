package on.ssgdeal.user_service.presentation.external.dto.destination;

public record CreateMyDestinationResponse(
    Long destinationId
) {

    public static CreateMyDestinationResponse from(Long id) {
        return new CreateMyDestinationResponse(id);
    }
}
