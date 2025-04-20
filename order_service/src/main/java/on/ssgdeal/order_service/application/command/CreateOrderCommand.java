package on.ssgdeal.order_service.application.command;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.command.Command;
import on.ssgdeal.order_service.application.command.support.OrderCommandSupport;
import on.ssgdeal.order_service.application.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.dto.CreateUserInfoDto;
import on.ssgdeal.order_service.application.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.domain.entity.Order;
import on.ssgdeal.order_service.domain.entity.OrderProduct;
import on.ssgdeal.order_service.domain.entity.Orderer;
import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.entity.TotalOrderPayment;
import on.ssgdeal.order_service.domain.entity.dtos.CreateOrderDto;
import on.ssgdeal.order_service.domain.entity.dtos.CreateOrderProductDto;
import on.ssgdeal.order_service.domain.entity.dtos.CreateTotalOrderDto;
import on.ssgdeal.order_service.domain.repository.TotalOrderRepository;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dtos.ValidDestinationResponseDto;
import on.ssgdeal.order_service.util.OrderNumberGenerator;

@Slf4j
@RequiredArgsConstructor
public class CreateOrderCommand implements Command<TotalOrder> {

    private final CreateOrderRequestDto request;
    private final LoginUserInfoDto loginUserInfoDto;
    private final TotalOrderRepository totalOrderRepository;
    private final OrderNumberGenerator orderNumberGenerator;
    private final OrderCommandSupport support;

    @Override
    public TotalOrder execute() {

        ValidDestinationResponseDto validDestinationResponseDto = support.validDestination(
            request.destinationId());

        GetProductInfoDto productInfo = support.validProductInfo(request);
        support.decreaseStock(productInfo);

        List<CreateOrderDto> orderDtos = getCreateOrderDtos(productInfo);
        String totalOrderNumber = getTotalOrderNumber();
        long totalOrderPrice = getTotalOrderPrice(orderDtos);

        // totalOrder 엔티티 생성
        CreateTotalOrderDto creteTotalOrderDto = CreateTotalOrderDto.from(totalOrderNumber,
            totalOrderPrice);
        TotalOrder totalOrder = TotalOrder.create(creteTotalOrderDto);

        // Orderer 생성
        CreateUserInfoDto createUserInfoDto = CreateUserInfoDto.from(request, loginUserInfoDto,
            validDestinationResponseDto);
        Orderer orderer = Orderer.create(totalOrder, createUserInfoDto);

        // totalOrderPayment 엔티티 생성
        TotalOrderPayment totalOrderPayment = TotalOrderPayment.create(totalOrder);

        // OrderProduct, Order 엔티티 생성
        List<Order> orders = savedOrdersAndOrderProducts(totalOrder, orderDtos);

        // totalOrder 저장
        totalOrder.addDependencies(orderer, totalOrderPayment, orders);
        TotalOrder savedTotalOrder = totalOrderRepository.save(totalOrder);

        log.info("주문 생성 완료 : {}", savedTotalOrder.getId());
        return savedTotalOrder;
    }

    @Override
    public void undo() {

    }

    private List<Order> savedOrdersAndOrderProducts(
        TotalOrder totalOrder,
        List<CreateOrderDto> orderDtos
    ) {
        List<Order> orders = Order.create(totalOrder, orderDtos);
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            CreateOrderDto createOrderDto = orderDtos.get(i);

            List<OrderProduct> orderProducts = createOrderDto.products().stream()
                .map(productDto -> OrderProduct.create(order, productDto)).toList();

            order.addOrderProductDependency(orderProducts);
        }
        return orders;
    }

    private long getTotalOrderPrice(List<CreateOrderDto> orderDtos) {
        return orderDtos.stream().mapToLong(CreateOrderDto::orderTotalPrice).sum();
    }

    private List<CreateOrderDto> getCreateOrderDtos(GetProductInfoDto productInfoAndStockDecrease) {
        return productInfoAndStockDecrease.companyList().stream()
            .map(companyInfo -> {
                List<CreateOrderProductDto> productDtos = companyInfo.companyProductList().stream()
                    .map(product -> {
                        Long totalPrice = (product.promotionPrice() + product.extraPrice())
                            * product.decreaseStockAmount();
                        return CreateOrderProductDto.from(product, totalPrice);
                    }).toList();

                Long orderTotalPrice = productDtos.stream()
                    .mapToLong(CreateOrderProductDto::totalPrice).sum();
                return CreateOrderDto.from(orderTotalPrice, companyInfo.companyId(),
                    companyInfo.companyName(), productDtos);
            }).toList();
    }

    private String getTotalOrderNumber() {
        return orderNumberGenerator.generateOrderNumber();
    }
}
