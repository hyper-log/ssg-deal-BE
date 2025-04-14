package on.ssgdeal.order_service.infrastructure.client.user.feign.dtos;

public record ValidDestinationResponseDto(Long destinationId, String address) {

    public static ValidDestinationResponseDto from(Long destinationId, String address) {
        return new ValidDestinationResponseDto(destinationId, address);
    }
}
