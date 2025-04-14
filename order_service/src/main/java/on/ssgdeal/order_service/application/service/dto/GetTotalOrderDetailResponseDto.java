package on.ssgdeal.order_service.application.service.dto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import on.ssgdeal.order_service.domain.entity.Order;
import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.entity.TotalOrderPayment;
import on.ssgdeal.order_service.domain.enums.OrderStatus;
import on.ssgdeal.order_service.domain.enums.PaymentMethod;
import on.ssgdeal.order_service.domain.enums.PaymentType;

public record GetTotalOrderDetailResponseDto(Long totalOrderId,
                                             LocalDateTime orderDateTime,
                                             String ordererName,
                                             String destination,
                                             GetTotalOrderDetailPaymentInfoDto paymentInfo,
                                             List<GetOrdersDetailInfoDto> subOrders
) {

    public static GetTotalOrderDetailResponseDto toGetTotalOrderDetailResponseDto(
        TotalOrder totalOrder
    ) {
        return new GetTotalOrderDetailResponseDto(
            totalOrder.getId(),
            totalOrder.getCreatedAt(),
            totalOrder.getOrderer().getNickname(),
            totalOrder.getOrderer().getDestination(),
            toGetTotalOrderDetailPaymentInfoDto(totalOrder),
            toGetSubOrders(totalOrder)
        );
    }

    private static GetTotalOrderDetailPaymentInfoDto toGetTotalOrderDetailPaymentInfoDto(
        TotalOrder totalOrder
    ) {
        return totalOrder.getTotalOrderPayments().stream()
            .max(Comparator.comparing(TotalOrderPayment::getCreatedAt)) // 가장 최근 결제 찾기
            .map(payment -> new GetTotalOrderDetailPaymentInfoDto(
                totalOrder.getPrice().getValue(),
                payment.getPaymentType(),
                payment.getPaymentMethod()
            ))
            .orElse(null);
    }

    private static List<GetOrdersDetailInfoDto> toGetSubOrders(TotalOrder totalOrder) {
        return totalOrder.getOrders().stream()
            .map(order -> new GetOrdersDetailInfoDto(
                order.getId(),
                order.getStatus(),
                order.getCompanyId(),
                order.getCompanyName(),
                toGetProducts(order)
            ))
            .toList();
    }

    private static List<GetProductDetailInfoDto> toGetProducts(Order order) {
        return order.getOrderProducts().stream()
            .map(product -> new GetProductDetailInfoDto(
                product.getId(),
                product.getProductName(),
                product.getOptionName(),
                product.getTotalPrice(),
                product.getQuantity().getValue(),
                product.getPreviewUrl()
            ))
            .toList();
    }

    public record GetTotalOrderDetailPaymentInfoDto(Long paymentTotalPrice,
                                                    PaymentType paymentType,
                                                    PaymentMethod paymentMethod
    ) {

    }

    public record GetOrdersDetailInfoDto(Long orderId,
                                         OrderStatus orderStatus,
                                         Long companyId,
                                         String companyName,
                                         List<GetProductDetailInfoDto> orderProducts
    ) {

    }

    public record GetProductDetailInfoDto(Long productId,
                                          String productName,
                                          String optionName,
                                          Long paymentPrice,
                                          Long quantity,
                                          String productPreview
    ) {

    }

}
