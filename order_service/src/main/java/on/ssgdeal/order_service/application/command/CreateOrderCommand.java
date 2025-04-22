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

        // 검증 및 재고 처리 단계
        ValidateResult validateResult = validateAndDecreaseStock();

        // 주문 데이터 준비 단계
        OrderPreparationData prepData = prepareOrderData(validateResult);

        // 엔티티 생성 및 저장 단계
        TotalOrder savedTotalOrder = createAndSaveTotalOrder(prepData);
        log.info("주문 생성 완료 : {}", savedTotalOrder);
        return savedTotalOrder;
    }

    @Override
    public void undo() {
        // 주문 생성 취소는 비즈니스 요구 사항에 따라
        // Outbox event 발행 예정입니다.
        log.warn("CreateOrderCommand undo 기능을 지원하지 않습니다.");
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

    // 검증 및 재고 감소를 처리하는 새 메서드
    private ValidateResult validateAndDecreaseStock() {
        ValidDestinationResponseDto validDestinationResponseDto = support.validateDestination(
            request.destinationId());

        GetProductInfoDto productInfo = support.validateProductInfo(request);
        support.decreaseStock(productInfo);

        return new ValidateResult(validDestinationResponseDto, productInfo);
    }

    // 주문 데이터를 준비하는 새 메서드
    private OrderPreparationData prepareOrderData(ValidateResult validateResult) {
        List<CreateOrderDto> orderDtos = getCreateOrderDtos(validateResult.productInfo());
        String totalOrderNumber = getTotalOrderNumber();
        long totalOrderPrice = getTotalOrderPrice(orderDtos);

        return new OrderPreparationData(orderDtos, totalOrderNumber, totalOrderPrice,
            validateResult);
    }

    // 엔티티를 생성하고 저장하는 새 메서드
    private TotalOrder createAndSaveTotalOrder(OrderPreparationData prepData) {
        // totalOrder 엔티티 생성
        CreateTotalOrderDto creteTotalOrderDto = CreateTotalOrderDto.from(
            prepData.totalOrderNumber(),
            prepData.totalOrderPrice());
        TotalOrder totalOrder = TotalOrder.create(creteTotalOrderDto);

        // Orderer 생성
        CreateUserInfoDto createUserInfoDto = CreateUserInfoDto.from(request, loginUserInfoDto,
            prepData.validateResult().validDestinationResponseDto());
        Orderer orderer = Orderer.create(totalOrder, createUserInfoDto);

        // totalOrderPayment 엔티티 생성
        TotalOrderPayment totalOrderPayment = TotalOrderPayment.create(totalOrder);

        // OrderProduct, Order 엔티티 생성
        List<Order> orders = savedOrdersAndOrderProducts(totalOrder, prepData.orderDtos());

        // totalOrder 저장
        totalOrder.addDependencies(orderer, totalOrderPayment, orders);
        return totalOrderRepository.save(totalOrder);
    }

    // 결과 객체
    private record ValidateResult(ValidDestinationResponseDto validDestinationResponseDto,
                                  GetProductInfoDto productInfo) {

    }

    private record OrderPreparationData(List<CreateOrderDto> orderDtos, String totalOrderNumber,
                                        long totalOrderPrice, ValidateResult validateResult) {

    }
}
