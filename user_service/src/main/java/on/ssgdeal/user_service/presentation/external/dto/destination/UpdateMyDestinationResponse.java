package on.ssgdeal.user_service.presentation.external.dto.destination;

import on.ssgdeal.user_service.domain.entity.Destination;

public record UpdateMyDestinationResponse(
    Long destinationId,
    String destinationName,
    String address
) {

    public static UpdateMyDestinationResponse from(Destination destination) {
        return new UpdateMyDestinationResponse(
            destination.getId(),
            destination.getName(),
            destination.getAddress().toString()
        );
    }
}
