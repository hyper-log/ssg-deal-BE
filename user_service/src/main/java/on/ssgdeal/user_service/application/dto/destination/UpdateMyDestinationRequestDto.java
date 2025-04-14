package on.ssgdeal.user_service.application.dto.destination;

import on.ssgdeal.user_service.domain.entity.Destination.UpdateDestinationDto;
import on.ssgdeal.user_service.domain.vo.Address;

public record UpdateMyDestinationRequestDto(
    Long destinationId,
    String address,
    String destinationName
) {

    public UpdateDestinationDto toUpdateDestinationDto() {
        return UpdateDestinationDto.builder()
            .address(Address.valueOf(address))
            .name(destinationName)
            .build();
    }

}
