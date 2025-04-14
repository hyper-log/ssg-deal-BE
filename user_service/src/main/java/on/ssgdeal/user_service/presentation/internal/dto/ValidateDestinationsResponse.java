package on.ssgdeal.user_service.presentation.internal.dto;

import on.ssgdeal.user_service.domain.entity.Destination;

public record ValidateDestinationsResponse(
    Long destinationId,
    String address
) {

    public static ValidateDestinationsResponse from(Destination destination) {
        return new ValidateDestinationsResponse(
            destination.getId(),
            destination.getAddress().toString()
        );
    }

}
