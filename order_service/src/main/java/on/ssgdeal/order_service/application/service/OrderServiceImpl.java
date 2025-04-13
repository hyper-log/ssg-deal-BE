package on.ssgdeal.order_service.application.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.order_service.application.service.dto.CancelOrderRequestDto;
import on.ssgdeal.order_service.application.service.dto.CancelOrderResponseDto;
import on.ssgdeal.order_service.application.service.dto.CancelTotalOrderRequestDto;
import on.ssgdeal.order_service.application.service.dto.CancelTotalOrderResponseDto;
import on.ssgdeal.order_service.application.service.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.service.dto.CreateUserInfoDto;
import on.ssgdeal.order_service.application.service.dto.GetTotalOrderDetailResponseDto;
import on.ssgdeal.order_service.application.service.dto.GetTotalOrdersResponseDto;
import on.ssgdeal.order_service.application.service.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.application.service.dto.TotalOrderProductInfo;
import on.ssgdeal.order_service.application.service.dto.TotalOrderProductInfo.ProductInfo;
import on.ssgdeal.order_service.application.service.dto.UpdateCancelOrderSuccessRequestDto;
import on.ssgdeal.order_service.application.service.dto.UpdateTotalOrderFailRequestDto;
import on.ssgdeal.order_service.application.service.dto.UpdateTotalOrderSuccessRequestDto;
import on.ssgdeal.order_service.domain.entity.Order;
import on.ssgdeal.order_service.domain.entity.OrderProduct;
import on.ssgdeal.order_service.domain.entity.Orderer;
import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.entity.TotalOrderPayment;
import on.ssgdeal.order_service.domain.entity.dtos.CreateOrderDto;
import on.ssgdeal.order_service.domain.entity.dtos.CreateOrderProductDto;
import on.ssgdeal.order_service.domain.entity.dtos.CreateTotalOrderDto;
import on.ssgdeal.order_service.domain.entity.dtos.GetTotalOrderDetailDto;
import on.ssgdeal.order_service.domain.entity.dtos.GetTotalOrdersUserInfoDto;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;
import on.ssgdeal.order_service.domain.entity.dtos.mapper.TotalOrderEntityLayerMapper;
import on.ssgdeal.order_service.domain.enums.OrderStatus;
import on.ssgdeal.order_service.domain.enums.TotalOrderStatus;
import on.ssgdeal.order_service.domain.repository.TotalOrderRepository;
import on.ssgdeal.order_service.exception.OrderException.OrderAlreadyCancelException;
import on.ssgdeal.order_service.exception.OrderException.OrderNotCancelException;
import on.ssgdeal.order_service.exception.OrderException.OrderNotFoundTotalOrderException;
import on.ssgdeal.order_service.exception.OrderException.OrderNotOrdererException;
import on.ssgdeal.order_service.exception.OrderException.OrderPaymentsError;
import on.ssgdeal.order_service.exception.OrderException.OrderPromotionStockOver;
import on.ssgdeal.order_service.exception.OrderException.OrderValidDestination;
import on.ssgdeal.order_service.infrastructure.client.cart.feign.dtos.ClearCartRequestDto;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelOrderPaymentRequestDto;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelTotalOrderPaymentRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.DecreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.DecreaseProductStockResponseDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoRequestDto.GetProductDetails;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.InCreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.slack.dtos.TotalOrderCompleteSendInfoDto;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.dtos.OrderCompleteSendSlackRequestDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dtos.ValidDestinationRequestDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dtos.ValidDestinationResponseDto;
import on.ssgdeal.order_service.presentation.external.dto.CreateOrderResponse;
import on.ssgdeal.order_service.presentation.internal.dto.ValidTotalOrderResponse;
import on.ssgdeal.order_service.util.OrderNumberGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "OrderService")
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final PromotionService promotionService;
    private final TotalOrderRepository totalOrderRepository;
    private final UserService userService;
    private final TotalOrderEntityLayerMapper totalOrderEntityLayerMapper;
    private final SlackService slackService;
    private final PaymentService paymentService;
    private final CartService cartService;
    private final OrderNumberGenerator orderNumberGenerator;

    @Override
    @Transactional
    public CreateOrderResponse createOrder(
        CreateOrderRequestDto request,
        LoginUserInfoDto loginUserInfoDto
    ) {

        log.info("주문 생성 요청: {}", request);

        // 배송지 검증
        ValidDestinationResponseDto validDestinationResponseDto = validDestinationRequestDto(
            request.destinationId());

        // 상품 정보 조회 요청
        var productInfo = getGetProductInfoAndStockDecreaseResponseDto(request);

        // 상품 재고 감소 요청
        productStockRequest(productInfo);

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
        totalOrderRepository.save(totalOrder);

        return CreateOrderResponse.from(totalOrder);
    }

    @Override
    @Transactional
    public void createTotalOrderPaymentSuccess(
        UpdateTotalOrderSuccessRequestDto requestDto,
        LoginUserInfoDto loginUserInfo
    ) {
        log.info("주문 성공 결제 요청 : {}", requestDto);
        TotalOrder totalOrder = getTotalOrderElseThrow(requestDto.totalOrderId());
        UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto = totalOrderEntityLayerMapper.toUpdateTotalOrderSuccessDto(
            requestDto);
        totalOrderRepository.paymentSuccess(totalOrder, updateTotalOrderSuccessDto);
        TotalOrder updatedTotalOrder = getTotalOrderElseThrow(totalOrder.getId());
        sendSlackMessage(loginUserInfo, updatedTotalOrder.getCreatedAt().toLocalDate(),
            updatedTotalOrder.getPrice().getValue(), updateTotalOrderSuccessDto);
        clearCart(updatedTotalOrder);
    }

    private void clearCart(TotalOrder updatedTotalOrder) {
        ClearCartRequestDto clearCartRequestDto = ClearCartRequestDto.from(updatedTotalOrder);
        try {
            log.info("장바구니 비우기 요청 totalOrderId : {}", updatedTotalOrder.getId());
            cartService.clearCart(clearCartRequestDto);
        } catch (Exception e) {
            log.error("장바구니 비우기 요청 실패 : {} ", e.getMessage());
        }
    }

    @Override
    public PageDto<GetTotalOrdersResponseDto> getTotalOrders(
        LoginUserInfoDto loginUserInfo,
        Pageable pageable
    ) {
        log.info("총 주문 리스트 조회 요청");
        GetTotalOrdersUserInfoDto getTotalOrdersUserInfoDto = totalOrderEntityLayerMapper.toGetTotalOrdersUserInfoDto(
            loginUserInfo);
        Page<TotalOrder> totalOrders = totalOrderRepository.getTotalOrderList(
            getTotalOrdersUserInfoDto, pageable);

        List<GetTotalOrdersResponseDto> totalMapperList = totalOrders.getContent().stream()
            .map(GetTotalOrdersResponseDto::toGetTotalOrdersResponseDto)
            .toList();

        Page<GetTotalOrdersResponseDto> totalMapperPage = new PageImpl<>(
            totalMapperList,
            totalOrders.getPageable(),
            totalOrders.getTotalElements()
        );

        return PageDto.from(totalMapperPage);
    }

    @Override
    public GetTotalOrderDetailResponseDto getTotalOrderDetail(Long totalOrderId,
        LoginUserInfoDto loginUserInfo) {
        log.info("주문 상세 내용 요청");
        GetTotalOrderDetailDto getTotalOrderDetailDto = GetTotalOrderDetailDto.from(totalOrderId,
            loginUserInfo.userId());
        TotalOrder totalOrder = totalOrderRepository.getTotalOrderDetail(getTotalOrderDetailDto);
        return GetTotalOrderDetailResponseDto.toGetTotalOrderDetailResponseDto(totalOrder);
    }

    @Override
    public ValidTotalOrderResponse validTotalOrder(Long totalOrderId) {
        boolean totalOrderExists = totalOrderRepository.existsById(totalOrderId);
        return ValidTotalOrderResponse.from(totalOrderExists);
    }

    @Override
    @Transactional
    public CancelTotalOrderResponseDto cancelTotalOrder(CancelTotalOrderRequestDto request) {
        TotalOrder totalOrder = totalOrderRepository.findTotalOrderForCancel(
            request.totalOrderId());

        if (!totalOrder.getOrderer().getUserId().equals(request.loginUserInfo().userId())) {
            throw new OrderNotOrdererException();

        } else if (!totalOrder.getStatus().equals(TotalOrderStatus.PAID)) {
            throw new OrderNotCancelException();
        }

        List<Order> nonPaidOrders = totalOrder.getOrders().stream()
            .filter(order -> order.getStatus() != OrderStatus.PAID)
            .toList();

        if (!nonPaidOrders.isEmpty()) {
            throw new OrderNotCancelException();
        }

        totalOrderRepository.cancelUpdateStatusTotalOrder(totalOrder);
        TotalOrder updateTotalOrder = totalOrderRepository.findTotalOrderForCancelUpdate(
            request.totalOrderId());

        requestCancelTotalOrderPayment(updateTotalOrder);
        requestCancelTotalOrderIncreaseProduct(updateTotalOrder);

        return CancelTotalOrderResponseDto.from(updateTotalOrder);
    }

    @Override
    @Transactional
    public void createTotalOrderPaymentFail(UpdateTotalOrderFailRequestDto requestDto) {
        TotalOrder totalOrder = totalOrderRepository.findTotalOrderForFail(
            requestDto.totalOrderId());
        totalOrderRepository.cancelUpdateStatusTotalOrder(totalOrder);
        requestCancelTotalOrderIncreaseProduct(totalOrder);
    }

    @Override
    @Transactional
    public CancelOrderResponseDto cancelOrder(CancelOrderRequestDto request) {
        TotalOrder totalOrder = totalOrderRepository.findOrderForCancel(
            request.totalOrderId(), request.orderId());
        if (totalOrder.getStatus().equals(TotalOrderStatus.CANCELED)
            || totalOrder.cancelAlreadyOrder(request.orderId())) {
            throw new OrderAlreadyCancelException();
        }
        totalOrder.cancelSpecificOrder(request.orderId());
        totalOrder.updateCancelTotalPrice(
            totalOrder.getOrders().get(0).getPrice().getValue());
        if (totalOrder.getPrice().getValue().equals(0L) || totalOrder.getPrice().getValue() < 0L) {
            totalOrder.updateCancelStatus();
        }
        requestCancelOrderPayment(totalOrder);
        requestCancelTotalOrderIncreaseProduct(totalOrder);
        return CancelOrderResponseDto.from(totalOrder.getOrders().get(0).getId());
    }

    @Override
    @Transactional
    public void cancelOrderPaymentSuccess(UpdateCancelOrderSuccessRequestDto request) {
        TotalOrder totalOrder = totalOrderRepository.findTotalOrderForCancel(
            request.totalOrderId());
        var updateCancelOrderSuccessDto = totalOrderEntityLayerMapper.toUpdateCancelOrderSuccessDto(
            request);
        totalOrder.addCancelPayment(updateCancelOrderSuccessDto);
    }

    private void requestCancelOrderPayment(TotalOrder totalOrder) {
        CancelOrderPaymentRequestDto requestDto = CancelOrderPaymentRequestDto.from(
            totalOrder.getOrders().get(0).getPrice().getValue());
        try {
            log.error("부분 주문 결제 취소 요청 cancelAmount : {}", requestDto.cancelAmount());
            paymentService.cancelOrderPayment(totalOrder.getId(), requestDto);
        } catch (Exception e) {
            log.error("부분 주문 결제 취소 오류 : {}", e.getMessage());
            throw new OrderPaymentsError();
        }
    }

    private void requestCancelTotalOrderIncreaseProduct(TotalOrder totalOrder) {
        List<InCreaseProductStockRequestDto> inCreaseProductStockRequestDtos = cancelIncreaseRequest(
            totalOrder);
        for (InCreaseProductStockRequestDto dto : inCreaseProductStockRequestDtos) {
            try {
                log.info("재고 증가 요청");
                promotionService.increaseProductStock(dto);
            } catch (Exception e) {
                log.error("재고 증가 요청 실패 : {} ", e.getMessage());
                log.error("재고 증가 요청 실패 상품 : {} ", dto.toString());
            }
        }
    }

    private void requestCancelTotalOrderPayment(TotalOrder totalOrder) {
        CancelTotalOrderPaymentRequestDto paymentRequestDto = CancelTotalOrderPaymentRequestDto.from(
            "총 결제 주문 취소", totalOrder.getPrice().getValue());

        try {
            log.info("총 주문 결제 취소 요청");
            paymentService.cancelTotalOrderPayment(totalOrder.getId(), paymentRequestDto);
        } catch (Exception e) {
            log.error("총 주문 결제 취소 오류 : {}", e.getMessage());
            throw new OrderPaymentsError();
        }
    }

    private void sendSlackMessage(
        LoginUserInfoDto loginUserInfo,
        LocalDate orderAt,
        Long paymentPrice,
        UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto
    ) {
        TotalOrderCompleteSendInfoDto totalOrderCompleteSendInfoDto = TotalOrderCompleteSendInfoDto.from(
            orderAt, paymentPrice);
        OrderCompleteSendSlackRequestDto sendSlackRequestDto = OrderCompleteSendSlackRequestDto.from(
            updateTotalOrderSuccessDto,
            totalOrderCompleteSendInfoDto, loginUserInfo);
        try {
            log.info("주문 성공 메시지 전달 요청");
            slackService.sendOrderCompleteMessage(sendSlackRequestDto);
        } catch (Exception e) {
            log.warn("슬랙 메시지 전송 실패: {}", e.getMessage());
        }
    }

    private TotalOrder getTotalOrderElseThrow(Long paymentId) {
        return totalOrderRepository.findById(paymentId)
            .orElseThrow(OrderNotFoundTotalOrderException::new);
    }

    protected ValidDestinationResponseDto validDestinationRequestDto(Long destinationId) {
        ValidDestinationRequestDto validDestinationRequest = ValidDestinationRequestDto.from(
            destinationId);
        try {
            log.info("배송지 검증 요청: {}", validDestinationRequest.destinationId());
            var validDestinationResponseDto = userService.validDestinationRequest(
                validDestinationRequest);
            log.info("배송지 검증 완료");
            return validDestinationResponseDto;
        } catch (Exception e) {
            log.info("배송지 검증 실패: {}", e.getMessage());
            throw new OrderValidDestination();
        }
    }

    protected void productStockRequest(GetProductInfoDto productInfo) {
        List<DecreaseProductStockRequestDto> decreaseRequest = toDecreaseRequest(productInfo);
        List<DecreaseProductStockResponseDto> decreaseSuccessList = new ArrayList<>();
        try {
            for (DecreaseProductStockRequestDto decreaseRequestDto : decreaseRequest) {
                log.info("재고 감소 요청 productId: {}", decreaseRequestDto.productId());
                DecreaseProductStockResponseDto decreaseProductStockResponseDto = promotionService.decreaseProductStock(
                    decreaseRequestDto);
                decreaseSuccessList.add(decreaseProductStockResponseDto);
                log.info("재고 감소 요청 성공 productId: {}", decreaseProductStockResponseDto.productId());
            }
        } catch (Exception e) {
            log.info("재고 감소 요청 실패, 롤백 요청 : {}", e.getMessage());
            List<InCreaseProductStockRequestDto> increaseRequest = toIncreaseRequest(
                decreaseSuccessList);
            for (InCreaseProductStockRequestDto increaseRequestDto : increaseRequest) {
                log.info("롤백 요청 productId: {}", increaseRequestDto.productId());
                promotionService.increaseProductStock(increaseRequestDto);
            }
        }
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

    private List<CreateOrderDto> getCreateOrderDtos(GetProductInfoDto productInfoAndStockDecrease) {
        List<CreateOrderDto> orderDtos = productInfoAndStockDecrease.companyList().stream()
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
        return orderDtos;
    }

    private long getTotalOrderPrice(List<CreateOrderDto> orderDtos) {
        return orderDtos.stream().mapToLong(CreateOrderDto::orderTotalPrice).sum();
    }

    private String getTotalOrderNumber() {
        return orderNumberGenerator.generateOrderNumber();
    }

    private GetProductInfoDto getGetProductInfoAndStockDecreaseResponseDto(
        CreateOrderRequestDto request
    ) {
        TotalOrderProductInfo totalOrderProductInfo = convertPromotionRequestProductInfo(request);
        var promotionRequestInfo = fromTotalOrderProductInfo(totalOrderProductInfo);

        GetProductInfoDto productInfoDto;
        try {
            log.info("Order > Promotion");
            log.info("주문 생성 상품 정보 조회 요청 : {}", promotionRequestInfo);
            productInfoDto = promotionService.getProductInfoAndStockDecrease(promotionRequestInfo);
            log.info("상품 정보 조회 성공 : {}", productInfoDto);
        } catch (Exception e) {
            log.info("조회 실패 및 재고 감소 불가능 상황 : {}", e.getMessage());
            throw new OrderPromotionStockOver();
        }
        return productInfoDto;
    }

    private TotalOrderProductInfo convertPromotionRequestProductInfo(
        CreateOrderRequestDto request
    ) {
        return new TotalOrderProductInfo(request.subOrders().stream().flatMap(
            sub -> sub.orderedProducts().stream()
                .map(p -> new ProductInfo(p.productId(), p.optionId(), p.quantity()))).toList());
    }

    protected GetProductInfoRequestDto fromTotalOrderProductInfo(
        TotalOrderProductInfo totalOrderProductInfo) {
        List<GetProductDetails> details = totalOrderProductInfo.products().stream().map(
            p -> new GetProductInfoRequestDto.GetProductDetails(p.productId(), p.optionId(),
                p.quantity())).toList();

        return new GetProductInfoRequestDto(details);
    }

    protected List<DecreaseProductStockRequestDto> toDecreaseRequest(
        GetProductInfoDto getProductInfoDto) {
        return getProductInfoDto.companyList().stream()
            .flatMap(company -> company.companyProductList().stream()).map(
                product -> DecreaseProductStockRequestDto.from(product.productId(),
                    product.optionId(), product.decreaseStockAmount())).toList();

    }

    protected List<InCreaseProductStockRequestDto> toIncreaseRequest(
        List<DecreaseProductStockResponseDto> decreaseSuccessList) {
        return decreaseSuccessList.stream().map(
            product -> InCreaseProductStockRequestDto.from(product.productId(), product.optionId(),
                product.decreaseStockAmount())).toList();
    }

    protected List<InCreaseProductStockRequestDto> cancelIncreaseRequest(TotalOrder totalOrder) {
        return totalOrder.getOrders().stream().flatMap(
                order -> order.getOrderProducts().stream().map(
                    product -> InCreaseProductStockRequestDto.from(product.getProductId(),
                        product.getOptionId(), product.getQuantity().getValue())
                )
            )
            .toList();
    }

}
