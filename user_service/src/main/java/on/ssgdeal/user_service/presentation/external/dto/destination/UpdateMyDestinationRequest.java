package on.ssgdeal.user_service.presentation.external.dto.destination;

import on.ssgdeal.user_service.application.dto.destination.UpdateMyDestinationRequestDto;

public record UpdateMyDestinationRequest(
    String address,
    String destinationName
) {

    public UpdateMyDestinationRequestDto toDto(Long destinationId) {
        return new UpdateMyDestinationRequestDto(
            destinationId,
            address,
            destinationName
        );
    }
}
