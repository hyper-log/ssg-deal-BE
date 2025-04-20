package on.ssgdeal.order_service.application.dto;

import java.sql.Timestamp;
import java.util.List;
import on.ssgdeal.order_service.domain.entity.Order;
import on.ssgdeal.order_service.domain.entity.OrderProduct;
import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.enums.OrderStatus;

public record GetTotalOrdersResponseDto(Long totalOrderId, Timestamp orderDateTime,
                                        List<GetSubOrderResponseDto> subOrders) {

    public static GetTotalOrdersResponseDto from(Long orderId, Timestamp orderDateTime,
        List<GetSubOrderResponseDto> subOrders) {
        return new GetTotalOrdersResponseDto(orderId, orderDateTime, subOrders);
    }

    public static GetTotalOrdersResponseDto toGetTotalOrdersResponseDto(TotalOrder totalOrder) {
        return from(
            totalOrder.getId(),
            Timestamp.valueOf(totalOrder.getCreatedAt()),
            totalOrder.getOrders().stream()
                .map(GetTotalOrdersResponseDto::toSubOrderDto)
                .toList()
        );
    }

    private static GetSubOrderResponseDto toSubOrderDto(Order order) {
        return new GetSubOrderResponseDto(
            order.getId(),
            order.getStatus(),
            order.getCompanyId(),
            order.getCompanyName(),
            order.getOrderProducts().stream()
                .map(GetTotalOrdersResponseDto::toOrderProductDto)
                .toList()
        );
    }

    private static GetOrderProductResponseDto toOrderProductDto(OrderProduct orderProduct) {
        return new GetOrderProductResponseDto(
            orderProduct.getId(),
            orderProduct.getProductName(),
            orderProduct.getTotalPrice(),
            orderProduct.getQuantity().getValue(),
            orderProduct.getPreviewUrl()
        );
    }

    public record GetSubOrderResponseDto(Long orderId, OrderStatus orderStatus, Long companyId,
                                         String companyName,
                                         List<GetOrderProductResponseDto> orderProducts) {

    }

    public record GetOrderProductResponseDto(Long productId, String productName, Long paymentPrice,
                                             Long quantity, String productPreview) {

    }
}
