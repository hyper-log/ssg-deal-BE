package on.ssgdeal.order_service.domain.entity.dtos;

public record GetTotalOrderDetailDto(Long totalOrderId, Long userId) {

    public static GetTotalOrderDetailDto from(Long totalOrderId, Long userId) {
        return new GetTotalOrderDetailDto(totalOrderId, userId);
    }

}
