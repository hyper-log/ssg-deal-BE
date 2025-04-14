package on.ssgdeal.order_service.domain.entity.dtos;

import java.util.List;

public record CreateOrderDto(
    Long orderTotalPrice,
    Long companyId,
    String companyName,
    List<CreateOrderProductDto> products
) {

    public static CreateOrderDto from(
        Long orderTotalPrice,
        Long companyId,
        String companyName,
        List<CreateOrderProductDto> products
    ) {
        return new CreateOrderDto(orderTotalPrice, companyId, companyName, products);
    }

}
