package on.ssgdeal.user_service.presentation.external.dto.destination;

import on.ssgdeal.user_service.application.dto.destination.CreateMyDestinationRequestDto;

public record CreateMyDestinationRequest(
    String destinationName,
    String address
) {

    public CreateMyDestinationRequestDto toDto() {
        return new CreateMyDestinationRequestDto(
            destinationName,
            address
        );
    }
}