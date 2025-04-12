package on.ssgdeal.order_service.infrastructure.client.user.feign.dtos;

public record ValidDestinationRequestDto(Long destinationId) {

    public static ValidDestinationRequestDto from(Long destinationId) {
        return new ValidDestinationRequestDto(destinationId);
    }
}
