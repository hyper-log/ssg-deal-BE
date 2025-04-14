package on.ssgdeal.order_service.domain.entity.dtos;

public record CreateTotalOrderDto(String totalOrderNumber, Long price) {

    public static CreateTotalOrderDto from(String totalOrderNumber, Long price) {
        return new CreateTotalOrderDto(totalOrderNumber, price);
    }
}
