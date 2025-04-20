package on.ssgdeal.order_service.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.common.auth.enums.AuthRole;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.pageable.enums.PageSortBy;
import on.ssgdeal.order_service.application.dto.CancelOrderRequestDto;
import on.ssgdeal.order_service.application.dto.CancelOrderResponseDto;
import on.ssgdeal.order_service.application.dto.CancelTotalOrderRequestDto;
import on.ssgdeal.order_service.application.dto.CancelTotalOrderResponseDto;
import on.ssgdeal.order_service.application.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.dto.GetTotalOrderDetailResponseDto;
import on.ssgdeal.order_service.application.dto.GetTotalOrdersResponseDto;
import on.ssgdeal.order_service.application.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.application.dto.UpdateTotalOrderSuccessRequestDto;
import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;
import on.ssgdeal.order_service.domain.entity.dtos.mapper.TotalOrderEntityLayerMapper;
import on.ssgdeal.order_service.domain.enums.PaymentMethod;
import on.ssgdeal.order_service.domain.enums.PaymentType;
import on.ssgdeal.order_service.domain.enums.TotalOrderStatus;
import on.ssgdeal.order_service.domain.repository.TotalOrderRepository;
import on.ssgdeal.order_service.exception.OrderException;
import on.ssgdeal.order_service.exception.OrderException.OrderAlreadyCancelException;
import on.ssgdeal.order_service.exception.OrderException.OrderNotFoundTotalOrderException;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelOrderPaymentRequestDto;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelOrderPaymentResponseDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.DecreaseProductStockResponseDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.slack.dtos.TotalOrderCompleteSendInfoDto;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.dtos.OrderCompleteSendSlackRequestDto;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.dtos.OrderCompleteSendSlackResponseDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dtos.ValidDestinationRequestDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dtos.ValidDestinationResponseDto;
import on.ssgdeal.order_service.presentation.external.dto.CreateOrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql(statements = "ALTER TABLE total_order AUTO_INCREMENT = 1",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@DisplayName("OrderServiceImpl 클래스의")
class OrderServiceImplTest {

    @Autowired
    EntityManager em;
    @MockitoBean
    private AuditorAware<Long> auditorAware;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderServiceImpl orderServiceImpl;
    @MockitoBean
    private PromotionService promotionService;
    @Autowired
    private TotalOrderRepository totalOrderRepository;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private SlackService slackService;
    @Autowired
    private TotalOrderEntityLayerMapper totalOrderEntityLayerMapper;
    @MockitoBean
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        Mockito.when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of(999L));
    }

    private LoginUserInfoDto createFakeLoginUserInfo() {
        Passport passport = new Passport(999L, "실패 케이스", AuthRole.CONSUMER, "한나윤",
            "order@naver.com");
        return LoginUserInfoDto.from(passport);
    }

    private LoginUserInfoDto createFakeValidLoginUserInfo() {
        Passport passport = new Passport(1000L, "제발 돼라", AuthRole.CONSUMER, "한나윤",
            "order@naver.com");
        return LoginUserInfoDto.from(passport);
    }

    private CreateOrderRequestDto createFakeCreateOrderRequestDto() {
        var orderedProduct1 = new CreateOrderRequestDto.CreateSubOrderRequestDto.OrderedProductDto(
            1L, 101L, 2L // productId, optionId, quantity
        );

        var orderedProduct2 = new CreateOrderRequestDto.CreateSubOrderRequestDto.OrderedProductDto(
            2L, 102L, 1L
        );

        var subOrder1 = new CreateOrderRequestDto.CreateSubOrderRequestDto(
            List.of(orderedProduct1));
        var subOrder2 = new CreateOrderRequestDto.CreateSubOrderRequestDto(
            List.of(orderedProduct2));

        return new CreateOrderRequestDto(
            1L,
            "문 앞에 놔주세요",
            List.of(subOrder1, subOrder2)
        );
    }

    private GetProductInfoDto createFakePromotionResponse() {
        var product1 = new GetProductInfoDto.CompanyProduct(
            "PROMOTION_APPLIED", 1L, "딸기", "strawberry.jpg",
            5000L, 4000L, 101L, "기본 옵션", 500L, 2L
        );

        var product2 = new GetProductInfoDto.CompanyProduct(
            "PROMOTION_APPLIED", 2L, "바나나", "banana.jpg",
            3000L, 2500L, 102L, "대용량 옵션", 300L, 1L
        );

        var company = new GetProductInfoDto.CompanyInfo(
            100L, "싱싱과일", List.of(product1, product2)
        );

        return new GetProductInfoDto(List.of(company));
    }

    private GetProductInfoDto createFakeGetProductInfoDto() {
        var product1 = new GetProductInfoDto.CompanyProduct(
            "ON_SALE", 1L, "상품1", "프리뷰1", 10000L, 8000L,
            101L, "옵션1", 0L, 1L
        );
        var product2 = new GetProductInfoDto.CompanyProduct(
            "ON_SALE", 2L, "상품2", "프리뷰2", 12000L, 9000L,
            102L, "옵션2", 0L, 1L
        );
        var product3 = new GetProductInfoDto.CompanyProduct(
            "ON_SALE", 3L, "상품3", "프리뷰3", 15000L, 10000L,
            103L, "옵션3", 0L, 1L
        );

        var companyInfo = new GetProductInfoDto.CompanyInfo(
            1L, "회사1", List.of(product1, product2, product3)
        );
        return GetProductInfoDto.from(List.of(companyInfo));
    }

    private ValidDestinationResponseDto createFakeValidDestinationResponseDto() {
        return ValidDestinationResponseDto.from(1L, "서울시 광진구");
    }

    private DecreaseProductStockResponseDto createFakeDecreaseProductResponse() {
        return DecreaseProductStockResponseDto.from(1L, 101L, 2L);
    }

    private UpdateTotalOrderSuccessRequestDto createFakeTotalOrderPaymentSuccess() {
        return new UpdateTotalOrderSuccessRequestDto(1L, 1L, PaymentType.TOSS, PaymentMethod.CARD,
            20000L,
            LocalDateTime.now(), "2");
    }

    private UpdateTotalOrderSuccessRequestDto createFakeTotalOrderPaymentSuccessValid() {
        return new UpdateTotalOrderSuccessRequestDto(0L, 1L, PaymentType.TOSS, PaymentMethod.CARD,
            20000L,
            LocalDateTime.now(), "2");
    }

    private OrderCompleteSendSlackRequestDto createFakerOrderCompleteSendSlackRequestDto(
        UpdateTotalOrderSuccessRequestDto updateTotalOrderSuccessRequestDto,
        LoginUserInfoDto loginUserInfoDto) {
        UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto = totalOrderEntityLayerMapper.toUpdateTotalOrderSuccessDto(
            updateTotalOrderSuccessRequestDto);
        TotalOrderCompleteSendInfoDto sendInfoDto = TotalOrderCompleteSendInfoDto.from(
            LocalDate.now(),
            10000L);
        return OrderCompleteSendSlackRequestDto.from(updateTotalOrderSuccessDto, sendInfoDto,
            loginUserInfoDto);
    }

    private OrderCompleteSendSlackResponseDto createFakeOrderCompleteSendSlackResponseDto() {
        return new OrderCompleteSendSlackResponseDto(1L);
    }

    @Nested
    @DisplayName("Describe: createOrder 메서드는")
    class createOrderTest {

        @Nested
        @DisplayName("Context: 필수 입력과 입력 값 검증이 성공했을 때")
        class createSuccessTest {

            @Test
            @DisplayName("It: 데이터를 저장하여 OrderId, TotalOrderPrice 반환한다.")
            void test() throws Exception {
                //given
                var request = createFakeCreateOrderRequestDto();
                var loginUserInfo = createFakeLoginUserInfo();
                var validRequest = ValidDestinationRequestDto.from(1L);
                given(userService.validDestinationRequest(validRequest))
                    .willReturn(createFakeValidDestinationResponseDto());
                given(promotionService.getProductInfoAndStockDecrease(any()))
                    .willReturn(createFakePromotionResponse());
                given(promotionService.decreaseProductStock(any()))
                    .willReturn(createFakeDecreaseProductResponse());

                //when
                var result = orderService.createOrder(request, loginUserInfo);

                //then
                assertThat(result).isNotNull();
                assertThat(result.price()).isNotNull();

                TotalOrder totalOrder = totalOrderRepository.findById(result.orderId())
                    .orElseThrow(null);
                assertThat(result.orderId()).isEqualTo(totalOrder.getId());
            }
        }

        @Nested
        @DisplayName("Context: 필수 입력 받고 배송지 검증이 실패했을 때")
        class createFailValidDestinationTest {

            @Test
            @DisplayName("It: OrderException 에러를 반환한다.")
            void test() throws Exception {
                //given
                var request = createFakeCreateOrderRequestDto();
                var loginUserInfo = createFakeLoginUserInfo();
                var validRequest = ValidDestinationRequestDto.from(1L);
                given(userService.validDestinationRequest(validRequest))
                    .willThrow(new RuntimeException());

                //when & then
                assertThatThrownBy(() -> orderService.createOrder(request, loginUserInfo))
                    .isInstanceOf(OrderException.class);
            }
        }

        @Nested
        @DisplayName("Context: 상품 정보 요청이 실패했을 때")
        class createFailGetPromotionInfoTest {

            @Test
            @DisplayName("It: OrderException 에러를 반환한다.")
            void test() throws Exception {
                //given
                var request = createFakeCreateOrderRequestDto();
                var loginUserInfo = createFakeLoginUserInfo();
                var validRequest = ValidDestinationRequestDto.from(1L);
                given(userService.validDestinationRequest(validRequest))
                    .willReturn(createFakeValidDestinationResponseDto());
                given(promotionService.getProductInfoAndStockDecrease(any()))
                    .willThrow(new RuntimeException());

                //when & then
                assertThatThrownBy(() -> orderService.createOrder(request, loginUserInfo))
                    .isInstanceOf(OrderException.class);
            }
        }

        @Nested
        @DisplayName("Context: 상품 재고 감소 요청이 도중에 실패했을 때")
        class createFailPromotionDecreaseInfoTest {

            @Test
            @DisplayName("It: 성공한 상품의 개수만큼 롤백을 요청한다.")
            void test() throws Exception {
                // given
                var productInfo = createFakeGetProductInfoDto();
                var decreaseRequests = orderServiceImpl.toDecreaseRequest(productInfo);

                // 첫 번째, 두 번째 성공
                given(promotionService.decreaseProductStock(decreaseRequests.get(0)))
                    .willReturn(new DecreaseProductStockResponseDto(1L, 101L, 2L));
                given(promotionService.decreaseProductStock(decreaseRequests.get(1)))
                    .willReturn(new DecreaseProductStockResponseDto(2L, 102L, 1L));

                // 세 번째는 실패
                given(promotionService.decreaseProductStock(decreaseRequests.get(2)))
                    .willThrow(new RuntimeException());

                // when
                orderServiceImpl.productStockRequest(productInfo);

                // then
                // 롤백 요청이 두 번 호출되었는지 검증
                verify(promotionService, times(1)).increaseProductStock(
                    argThat(dto -> dto.productId().equals(1L))
                );
                verify(promotionService, times(1)).increaseProductStock(
                    argThat(dto -> dto.productId().equals(2L))
                );

                // 실패한 세 번째는 롤백 호출 안 됨
                verify(promotionService, never()).increaseProductStock(
                    argThat(dto -> dto.productId().equals(3L)));
            }
        }
    }

    @Nested
    @DisplayName("Describe: createTotalOrderPaymentSuccess 메서드는")
    class createTotalOrderPaymentSuccess {

        @Nested
        @DisplayName("Context: 입력 값 검증이 성공했을 때")
        class createSuccessTest {

            @Test
            @DisplayName("It: TotalOrder, Order, TotalOrderPayment 상태를 성공으로 갱신하고 슬랙 메시지 알림을 보낸다.")
            void test() throws Exception {

                //given
                var orderRequest = createFakeCreateOrderRequestDto();
                var orderLoginUserInfo = createFakeLoginUserInfo();

                given(userService.validDestinationRequest(any()))
                    .willReturn(createFakeValidDestinationResponseDto());
                given(promotionService.getProductInfoAndStockDecrease(any()))
                    .willReturn(createFakePromotionResponse());
                given(promotionService.decreaseProductStock(any()))
                    .willReturn(createFakeDecreaseProductResponse());

                orderService.createOrder(orderRequest, orderLoginUserInfo);

                var request = createFakeTotalOrderPaymentSuccess();
                var loginUserInfo = createFakeLoginUserInfo();
                var createOrderCompleteSendSlackRequestDto = createFakerOrderCompleteSendSlackRequestDto(
                    request, loginUserInfo);
                given(slackService.sendOrderCompleteMessage(createOrderCompleteSendSlackRequestDto))
                    .willReturn(createFakeOrderCompleteSendSlackResponseDto());

                //when
                orderService.createTotalOrderPaymentSuccess(request, loginUserInfo);

                //then
                TotalOrder totalOrder = totalOrderRepository.findById(request.totalOrderId())
                    .orElseThrow(null);

                assertThat(totalOrder.getStatus()).isEqualTo(TotalOrderStatus.PAID);
            }
        }

        @Nested
        @DisplayName("Context: 입력 값 검증이 실패했을 때")
        class FailTest {

            @Test
            @DisplayName("It: 오류를 반환한다.")
            void test() throws Exception {

                //given
                var orderRequest = createFakeCreateOrderRequestDto();
                var orderLoginUserInfo = createFakeLoginUserInfo();

                given(userService.validDestinationRequest(any()))
                    .willReturn(createFakeValidDestinationResponseDto());
                given(promotionService.getProductInfoAndStockDecrease(any()))
                    .willReturn(createFakePromotionResponse());
                given(promotionService.decreaseProductStock(any()))
                    .willReturn(createFakeDecreaseProductResponse());

                orderService.createOrder(orderRequest, orderLoginUserInfo);

                var request = createFakeTotalOrderPaymentSuccessValid();
                var loginUserInfo = createFakeLoginUserInfo();

                //when & then
                assertThatThrownBy(
                    () -> orderService.createTotalOrderPaymentSuccess(request, loginUserInfo))
                    .isInstanceOf(OrderNotFoundTotalOrderException.class);
            }
        }
    }

    @Nested
    @DisplayName("Describe: getTotalOrders 메서드는")
    class getListTotalOrder {

        @Nested
        @DisplayName("Context: 들어온 유저와 일치했을 때")
        class successTest {

            @Test
            @DisplayName("It: 유저의 주문 리스트를 반환한다. ")
            void test() throws Exception {

                //given
                var orderRequest = createFakeCreateOrderRequestDto();
                var orderLoginUserInfo = createFakeLoginUserInfo();

                given(userService.validDestinationRequest(any()))
                    .willReturn(createFakeValidDestinationResponseDto());
                given(promotionService.getProductInfoAndStockDecrease(any()))
                    .willReturn(createFakePromotionResponse());
                given(promotionService.decreaseProductStock(any()))
                    .willReturn(createFakeDecreaseProductResponse());

                orderService.createOrder(orderRequest, orderLoginUserInfo);

                var loginUserInfo = createFakeLoginUserInfo();
                Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC,
                    String.valueOf(PageSortBy.CREATED_AT));

                // when
                PageDto<GetTotalOrdersResponseDto> result = orderService.getTotalOrders(
                    loginUserInfo, pageable);

                // then
                assertThat(result).isNotNull();
                assertThat(result.content()).hasSize(1);
                assertThat(result.content().get(0).totalOrderId()).isEqualTo(1L);
                assertThat(result.totalElements()).isEqualTo(1);
                log.info(result.content().toString());
            }

            @Nested
            @DisplayName("Context: 유저가 주문 목록이 없다면")
            class failTest {

                @Test
                @DisplayName("It: 빈 리스트를 반환한다.")
                void test() throws Exception {

                    //given
                    var orderRequest = createFakeCreateOrderRequestDto();
                    var orderLoginUserInfo = createFakeLoginUserInfo();

                    given(userService.validDestinationRequest(any()))
                        .willReturn(createFakeValidDestinationResponseDto());
                    given(promotionService.getProductInfoAndStockDecrease(any()))
                        .willReturn(createFakePromotionResponse());
                    given(promotionService.decreaseProductStock(any()))
                        .willReturn(createFakeDecreaseProductResponse());

                    orderService.createOrder(orderRequest, orderLoginUserInfo);

                    var loginUserInfo = createFakeValidLoginUserInfo();
                    Pageable pageable = PageRequest.of(0, 10);

                    // when
                    PageDto<GetTotalOrdersResponseDto> result = orderService.getTotalOrders(
                        loginUserInfo, pageable);

                    // then
                    assertThat(result.content()).hasSize(0);
                }
            }
        }
    }

    @Nested
    @DisplayName("Describe: getTotalOrders 메서드는")
    class getDetailTotalOrder {

        @Nested
        @DisplayName("Context: 들어온 유저와 일치했을 때")
        class successTest {

            @Test
            @DisplayName("It: 유저의 주문 상세 정보를 반환한다. ")
            void test() throws Exception {

                //given
                var orderRequest = createFakeCreateOrderRequestDto();
                var orderLoginUserInfo = createFakeLoginUserInfo();

                given(userService.validDestinationRequest(any()))
                    .willReturn(createFakeValidDestinationResponseDto());
                given(promotionService.getProductInfoAndStockDecrease(any()))
                    .willReturn(createFakePromotionResponse());
                given(promotionService.decreaseProductStock(any()))
                    .willReturn(createFakeDecreaseProductResponse());

                orderService.createOrder(orderRequest, orderLoginUserInfo);

                var loginUserInfo = createFakeLoginUserInfo();

                // when
                GetTotalOrderDetailResponseDto result = orderService.getTotalOrderDetail(1L,
                    loginUserInfo);

                // then
                assertThat(result).isNotNull();
                assertThat(result.totalOrderId()).isEqualTo(1L);
                assertThat(result.destination()).isEqualTo("서울시 광진구");
            }
        }

        @Nested
        @DisplayName("Context: 들어온 유저와 일치하지 않으면")
        class failTest {

            @Test
            @DisplayName("It: 에러를 반환한다.")
            void test() throws Exception {

                //given
                var orderRequest = createFakeCreateOrderRequestDto();
                var orderLoginUserInfo = createFakeLoginUserInfo();

                given(userService.validDestinationRequest(any()))
                    .willReturn(createFakeValidDestinationResponseDto());
                given(promotionService.getProductInfoAndStockDecrease(any()))
                    .willReturn(createFakePromotionResponse());
                given(promotionService.decreaseProductStock(any()))
                    .willReturn(createFakeDecreaseProductResponse());

                orderService.createOrder(orderRequest, orderLoginUserInfo);

                var loginUserInfo = createFakeValidLoginUserInfo();

                //when & then
                assertThatThrownBy(
                    () -> orderService.getTotalOrderDetail(1L,
                        loginUserInfo))
                    .isInstanceOf(OrderNotFoundTotalOrderException.class);
            }
        }
    }

    @Nested
    @DisplayName("Describe: cancelOrder 메서드는")
    class cancelOrderTest {

        @Nested
        @DisplayName("Context: 조건 만족(유저 일치, TotalOrder 상태가 주문 완료일 때) 상태면")
        class successTest {

            @Test
            @DisplayName("It: 해당 주문을 캔슬한다. ")
            void test() throws Exception {

                //BEFORE GIVEN
                var orderRequest = createFakeCreateOrderRequestDto();
                var orderLoginUserInfo = createFakeLoginUserInfo();

                given(userService.validDestinationRequest(any()))
                    .willReturn(createFakeValidDestinationResponseDto());
                given(promotionService.getProductInfoAndStockDecrease(any()))
                    .willReturn(createFakePromotionResponse());
                given(promotionService.decreaseProductStock(any()))
                    .willReturn(createFakeDecreaseProductResponse());

                CreateOrderResponse totalOrderResponse = orderService.createOrder(orderRequest,
                    orderLoginUserInfo);
                TotalOrder totalOrder = totalOrderRepository.findById(totalOrderResponse.orderId())
                    .get();
                UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto = new UpdateTotalOrderSuccessDto(
                    totalOrder.getId(), 1L, PaymentType.TOSS, PaymentMethod.CARD,
                    totalOrder.getPrice().getValue(), LocalDateTime.now(), "1");
                totalOrderRepository.paymentSuccess(totalOrder, updateTotalOrderSuccessDto);
                em.flush();
                em.clear();

                // API GIVEN
                var loginUserInfo = createFakeLoginUserInfo();
                CancelOrderRequestDto request = CancelOrderRequestDto.from(totalOrder.getId(),
                    totalOrder.getOrders().get(0).getId(), loginUserInfo);
                CancelOrderPaymentResponseDto paymentResponse = new CancelOrderPaymentResponseDto(
                    5L);

                CancelOrderPaymentRequestDto cancelRequest = CancelOrderPaymentRequestDto.from(
                    11000L);
                given(paymentService.cancelOrderPayment(1L, cancelRequest))
                    .willReturn(paymentResponse);

                // when
                CancelOrderResponseDto result = orderService.cancelOrder(request);

                // then
                assertThat(result).isNotNull();
            }
        }

        @Nested
        @DisplayName("Context: 주문이 이미 취소된 상태면")
        class failTest {

            @Test
            @DisplayName("It: 에러를 반환한다.")
            void test() throws Exception {

                //BEFORE GIVEN
                var orderRequest = createFakeCreateOrderRequestDto();
                var orderLoginUserInfo = createFakeLoginUserInfo();

                given(userService.validDestinationRequest(any()))
                    .willReturn(createFakeValidDestinationResponseDto());
                given(promotionService.getProductInfoAndStockDecrease(any()))
                    .willReturn(createFakePromotionResponse());
                given(promotionService.decreaseProductStock(any()))
                    .willReturn(createFakeDecreaseProductResponse());

                CreateOrderResponse totalOrderResponse = orderService.createOrder(orderRequest,
                    orderLoginUserInfo);
                TotalOrder totalOrder = totalOrderRepository.findById(totalOrderResponse.orderId())
                    .get();
                UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto = new UpdateTotalOrderSuccessDto(
                    totalOrder.getId(), 1L, PaymentType.TOSS, PaymentMethod.CARD,
                    totalOrder.getPrice().getValue(), LocalDateTime.now(), "1");
                totalOrderRepository.paymentSuccess(totalOrder, updateTotalOrderSuccessDto);
                totalOrder.cancelSpecificOrder(totalOrder.getOrders().get(0).getId());
                totalOrderRepository.save(totalOrder);
                em.flush();
                em.clear();

                // API GIVEN
                var loginUserInfo = createFakeLoginUserInfo();
                CancelOrderRequestDto request = CancelOrderRequestDto.from(totalOrder.getId(),
                    totalOrder.getOrders().get(0).getId(),
                    loginUserInfo);
                CancelOrderPaymentResponseDto paymentResponse = new CancelOrderPaymentResponseDto(
                    5L);

                CancelOrderPaymentRequestDto cancelRequest = CancelOrderPaymentRequestDto.from(
                    11000L);
                given(paymentService.cancelOrderPayment(1L, cancelRequest))
                    .willReturn(paymentResponse);

                // when & then
                assertThatThrownBy(
                    () -> orderService.cancelOrder(request))
                    .isInstanceOf(OrderAlreadyCancelException.class);
            }
        }
    }

    @Nested
    @DisplayName("Describe: cancelTotalOrder 메서드는")
    class cancelTotalOrderTest {

        @Nested
        @DisplayName("Context: 조건 만족(유저 일치, TotalOrder 상태가 주문 완료일 때) 상태면")
        class successTest {

            @Test
            @DisplayName("It: 해당 주문을 캔슬한다. ")
            void test() throws Exception {

                //BEFORE GIVEN
                var orderRequest = createFakeCreateOrderRequestDto();
                var orderLoginUserInfo = createFakeLoginUserInfo();

                given(userService.validDestinationRequest(any()))
                    .willReturn(createFakeValidDestinationResponseDto());
                given(promotionService.getProductInfoAndStockDecrease(any()))
                    .willReturn(createFakePromotionResponse());
                given(promotionService.decreaseProductStock(any()))
                    .willReturn(createFakeDecreaseProductResponse());

                CreateOrderResponse totalOrderResponse = orderService.createOrder(orderRequest,
                    orderLoginUserInfo);
                TotalOrder totalOrder = totalOrderRepository.findById(totalOrderResponse.orderId())
                    .get();
                UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto = new UpdateTotalOrderSuccessDto(
                    totalOrder.getId(), 1L, PaymentType.TOSS, PaymentMethod.CARD,
                    totalOrder.getPrice().getValue(), LocalDateTime.now(), "1");
                totalOrderRepository.paymentSuccess(totalOrder, updateTotalOrderSuccessDto);
                em.flush();
                em.clear();

                // API GIVEN
                var loginUserInfo = createFakeLoginUserInfo();
                CancelTotalOrderRequestDto request = CancelTotalOrderRequestDto.from(
                    totalOrder.getId(),
                    loginUserInfo);

                // when
                CancelTotalOrderResponseDto result = orderService.cancelTotalOrder(request);

                // then
                assertThat(result).isNotNull();
                assertThat(result.orderId()).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("Context: 주문이 이미 취소된 상태면")
        class failTest {

            @Test
            @DisplayName("It: 에러를 반환한다.")
            void test() throws Exception {

                //BEFORE GIVEN
                var orderRequest = createFakeCreateOrderRequestDto();
                var orderLoginUserInfo = createFakeLoginUserInfo();

                given(userService.validDestinationRequest(any()))
                    .willReturn(createFakeValidDestinationResponseDto());
                given(promotionService.getProductInfoAndStockDecrease(any()))
                    .willReturn(createFakePromotionResponse());
                given(promotionService.decreaseProductStock(any()))
                    .willReturn(createFakeDecreaseProductResponse());

                CreateOrderResponse totalOrderResponse = orderService.createOrder(orderRequest,
                    orderLoginUserInfo);
                TotalOrder totalOrder = totalOrderRepository.findById(totalOrderResponse.orderId())
                    .get();
                UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto = new UpdateTotalOrderSuccessDto(
                    totalOrder.getId(), 1L, PaymentType.TOSS, PaymentMethod.CARD,
                    totalOrder.getPrice().getValue(), LocalDateTime.now(), "1");
                totalOrderRepository.paymentSuccess(totalOrder, updateTotalOrderSuccessDto);
                totalOrderRepository.cancelUpdateStatusTotalOrder(totalOrder);
                em.flush();
                em.clear();

                // API GIVEN
                var loginUserInfo = createFakeLoginUserInfo();
                CancelTotalOrderRequestDto request = CancelTotalOrderRequestDto.from(
                    totalOrder.getId(),
                    loginUserInfo);

                // when & then
                assertThatThrownBy(
                    () -> orderService.cancelTotalOrder(request))
                    .isInstanceOf(OrderNotFoundTotalOrderException.class);
            }
        }
    }

}