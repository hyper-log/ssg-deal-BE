package on.ssgdeal.user_service.presentation.internal.dto;

import on.ssgdeal.user_service.application.dto.destination.ValidateDestinationsRequestDto;

public record ValidateDestinationsRequest(
    Long destinationId
) {

    public ValidateDestinationsRequestDto toDto() {
        return new ValidateDestinationsRequestDto(
            destinationId
        );
    }

}
