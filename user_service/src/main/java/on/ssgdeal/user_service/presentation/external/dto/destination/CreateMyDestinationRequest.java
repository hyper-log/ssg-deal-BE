package on.ssgdeal.user_service.presentation.external.dto.destination;

import on.ssgdeal.user_service.application.dto.destination.CreateMyDestinationDto;

public record CreateMyDestinationRequest(
    String destinationName,
    String address
) {

    public CreateMyDestinationDto toDto() {
        return new CreateMyDestinationDto(
            destinationName,
            address
        );
    }
}