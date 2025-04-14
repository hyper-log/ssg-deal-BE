package on.ssgdeal.user_service.application.dto.destination;

import on.ssgdeal.user_service.domain.entity.Destination.CreateDestinationDto;
import on.ssgdeal.user_service.domain.vo.Address;

public record CreateMyDestinationRequestDto(
    String destinationName,
    String address
) {

    public CreateDestinationDto toCreateDestinationDto() {
        return CreateDestinationDto.builder()
            .name(destinationName)
            .address(new Address(address))
            .build();
    }

}
