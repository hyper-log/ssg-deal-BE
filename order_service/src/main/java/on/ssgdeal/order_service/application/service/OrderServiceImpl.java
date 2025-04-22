package on.ssgdeal.order_service.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.common.command.ScopedCommandInvoker;
import on.ssgdeal.common.messaging.domain.entity.Outbox;
import on.ssgdeal.common.messaging.domain.entity.Outbox.AggregateType;
import on.ssgdeal.common.messaging.exception.EventJsonProcessingException;
import on.ssgdeal.order_service.application.command.CreateOrderCommand;
import on.ssgdeal.order_service.application.command.OrderCommandFactory;
import on.ssgdeal.order_service.application.dto.CancelOrderRequestDto;
import on.ssgdeal.order_service.application.dto.CancelOrderResponseDto;
import on.ssgdeal.order_service.application.dto.CancelTotalOrderRequestDto;
import on.ssgdeal.order_service.application.dto.CancelTotalOrderResponseDto;
import on.ssgdeal.order_service.application.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.dto.GetTotalOrderDetailResponseDto;
import on.ssgdeal.order_service.application.dto.GetTotalOrdersResponseDto;
import on.ssgdeal.order_service.application.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.application.dto.UpdateCancelOrderSuccessRequestDto;
import on.ssgdeal.order_service.application.dto.UpdateTotalOrderFailRequestDto;
import on.ssgdeal.order_service.application.dto.UpdateTotalOrderSuccessRequestDto;
import on.ssgdeal.order_service.domain.entity.Event;
import on.ssgdeal.order_service.domain.entity.Order;
import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.entity.dtos.GetTotalOrderDetailDto;
import on.ssgdeal.order_service.domain.entity.dtos.GetTotalOrdersUserInfoDto;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;
import on.ssgdeal.order_service.domain.entity.dtos.mapper.TotalOrderEntityLayerMapper;
import on.ssgdeal.order_service.domain.enums.EventType;
import on.ssgdeal.order_service.domain.enums.OrderStatus;
import on.ssgdeal.order_service.domain.enums.TotalOrderStatus;
import on.ssgdeal.order_service.domain.repository.TotalOrderRepository;
import on.ssgdeal.order_service.exception.OrderException.OrderAlreadyCancelException;
import on.ssgdeal.order_service.exception.OrderException.OrderCreateException;
import on.ssgdeal.order_service.exception.OrderException.OrderNotCancelException;
import on.ssgdeal.order_service.exception.OrderException.OrderNotFoundTotalOrderException;
import on.ssgdeal.order_service.exception.OrderException.OrderNotOrdererException;
import on.ssgdeal.order_service.exception.OrderException.OrderPaymentsError;
import on.ssgdeal.order_service.infrastructure.client.cart.feign.dtos.ClearCartRequestDto;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelOrderPaymentRequestDto;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelTotalOrderPaymentRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.DecreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.DecreaseProductStockResponseDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.InCreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.slack.dtos.TotalOrderCompleteSendInfoDto;
import on.ssgdeal.order_service.infrastructure.messaging.dtos.IncreaseStockEvent;
import on.ssgdeal.order_service.infrastructure.messaging.dtos.OrderSuccessNotificationEvent;
import on.ssgdeal.order_service.infrastructure.persistence.jpa.OutboxRepository;
import on.ssgdeal.order_service.presentation.external.dto.CreateOrderResponse;
import on.ssgdeal.order_service.presentation.internal.dto.ValidTotalOrderResponse;
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
    private final OrderCommandFactory orderCommandFactory;
    private final ScopedCommandInvoker commandInvoker;
    private final OutboxRepository outboxRepository;

    @Override
    @Transactional
    public CreateOrderResponse createOrder(
        CreateOrderRequestDto request,
        LoginUserInfoDto loginUserInfoDto
    ) {
        try {
            CreateOrderCommand command = orderCommandFactory.createOrderCommand(request,
                loginUserInfoDto);
            TotalOrder totalOrder = commandInvoker.executeCommand(command);
            log.info("주문 생성 성공 : {}", totalOrder.getId());
            return CreateOrderResponse.from(totalOrder);
        } catch (Exception e) {
            log.info("주문 생성 실패 : {}", e.getMessage());
            throw new OrderCreateException();
        }
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
            updatedTotalOrder.getPrice().getValue(), updateTotalOrderSuccessDto,
            updatedTotalOrder.getId());
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
        TotalOrder updateTotalOrder = totalOrderRepository.findTotalOrderForCancelUpdate(
            requestDto.totalOrderId());
        requestCancelTotalOrderIncreaseProduct(updateTotalOrder);
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
        // TODO : 동기 이벤트 임시 주석 처리
        // List<InCreaseProductStockRequestDto> inCreaseProductStockRequestDtos = cancelIncreaseRequest(totalOrder);
        List<IncreaseStockEvent> payload = toIncreaseStockEventDto(totalOrder);
        /*
        for (InCreaseProductStockRequestDto dto : inCreaseProductStockRequestDtos) {
            try {
                log.info("재고 증가 요청");
                promotionService.increaseProductStock(dto);
            } catch (Exception e) {
                log.error("재고 증가 요청 실패 : {} ", e.getMessage());
                log.error("재고 증가 요청 실패 상품 : {} ", dto.toString());
            }
        }
         */
        for (IncreaseStockEvent payloadEvent : payload) {
            Event<IncreaseStockEvent> event = new Event<>(
                EventType.INCREASE_STOCK_EVENT,
                payloadEvent,
                LocalDate.now().toString());

            String payloadJson;
            try {
                payloadJson = new ObjectMapper().writeValueAsString(event);
            } catch (JsonProcessingException e) {
                throw new EventJsonProcessingException();
            }
            try {
                Outbox outbox = Outbox.create(event.getEventType().getTopic(),
                    AggregateType.ORDER,
                    totalOrder.getId(),
                    payloadJson);
                outboxRepository.save(outbox);
            } catch (Exception e) {
                log.error("재고 증가 요청 실패 : {} ", e.getMessage());
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
        UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto,
        Long totalOrderId
    ) {
        TotalOrderCompleteSendInfoDto totalOrderCompleteSendInfoDto = TotalOrderCompleteSendInfoDto.from(
            orderAt, paymentPrice);
        // TODO: 동기 전송 임시 주석
        /*
        OrderCompleteSendSlackRequestDto sendSlackRequestDto = OrderCompleteSendSlackRequestDto.from(
            updateTotalOrderSuccessDto,
            totalOrderCompleteSendInfoDto, loginUserInfo);
         */
        OrderSuccessNotificationEvent payload =
            OrderSuccessNotificationEvent.from(
                updateTotalOrderSuccessDto,
                totalOrderCompleteSendInfoDto,
                loginUserInfo
            );
        Event<OrderSuccessNotificationEvent> event = new Event<>(
            EventType.ORDER_SUCCESS_NOTIFICATION_EVENT,
            payload,
            LocalDate.now().toString());

        String payloadJson;
        try {
            payloadJson = new ObjectMapper().writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new EventJsonProcessingException();
        }

        try {
            log.info("주문 성공 전달 알람 요청");
            // TODO: 동기 전송 임시 주석
            // slackService.sendOrderCompleteMessage(sendSlackRequestDto);
            Outbox outbox = Outbox.create(event.getEventType().getTopic(),
                AggregateType.ORDER,
                totalOrderId,
                payloadJson);
            outboxRepository.save(outbox);
        } catch (Exception e) {
            log.warn("슬랙 메시지 전송 실패: {}", e.getMessage());
        }
    }

    private TotalOrder getTotalOrderElseThrow(Long paymentId) {
        return totalOrderRepository.findById(paymentId)
            .orElseThrow(OrderNotFoundTotalOrderException::new);
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

    protected List<IncreaseStockEvent> toIncreaseStockEventDto(TotalOrder totalOrder) {
        return totalOrder.getOrders().stream().flatMap(
                order -> order.getOrderProducts().stream().map(
                    product -> IncreaseStockEvent.from(product.getProductId(),
                        product.getOptionId(), product.getQuantity().getValue())
                )
            )
            .toList();
    }

}
