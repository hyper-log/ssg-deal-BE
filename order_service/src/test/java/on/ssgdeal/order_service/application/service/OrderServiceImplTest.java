package on.ssgdeal.order_service.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import on.ssgdeal.common.auth.enums.AuthRole;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.order_service.application.service.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.service.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.application.service.dto.UpdateTotalOrderSuccessRequestDto;
import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;
import on.ssgdeal.order_service.domain.entity.dtos.mapper.TotalOrderEntityLayerMapper;
import on.ssgdeal.order_service.domain.enums.PaymentMethod;
import on.ssgdeal.order_service.domain.enums.PaymentStatus;
import on.ssgdeal.order_service.domain.enums.PaymentType;
import on.ssgdeal.order_service.domain.enums.TotalOrderStatus;
import on.ssgdeal.order_service.domain.repository.TotalOrderRepository;
import on.ssgdeal.order_service.exception.OrderException;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.DecreaseProductStockResponseDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.slack.dto.TotalOrderCompleteSendInfoDto;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.dto.OrderCompleteSendSlackRequestDto;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.dto.OrderCompleteSendSlackResponseDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dto.ValidDestinationRequestDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dto.ValidDestinationResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@DisplayName("OrderServiceImpl 클래스의")
class OrderServiceImplTest {

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

    private LoginUserInfoDto createFakeLoginUserInfo() {
        Passport passport = new Passport(1L, "제발 돼라", AuthRole.CONSUMER, "한나윤", "order@naver.com");
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
        return new UpdateTotalOrderSuccessRequestDto(3L, 1L, PaymentType.TOSS, PaymentMethod.CARD,
            20000L,
            Timestamp.valueOf(LocalDateTime.now()), "2", PaymentStatus.COMPLETED);
    }

    private UpdateTotalOrderSuccessRequestDto createFakeTotalOrderPaymentSuccessValid() {
        return new UpdateTotalOrderSuccessRequestDto(1L, 1L, PaymentType.TOSS, PaymentMethod.CARD,
            20000L,
            Timestamp.valueOf(LocalDateTime.now()), "2", PaymentStatus.COMPLETED);
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
            void createOrderTest() throws Exception {
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
            void createOrderTest() throws Exception {
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
            void createOrderTest() throws Exception {
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
            void createOrderTest() throws Exception {
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
            void createOrderTest() throws Exception {

                //given
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

                assertThat(totalOrder.getStatus()).isEqualTo(TotalOrderStatus.EXPIRED);
            }
        }

        @Nested
        @DisplayName("Context: 입력 값 검증이 실패했을 때")
        class createSuccessValidTest {

            @Test
            @DisplayName("It: 오류를 반환한다.")
            void createOrderTest() throws Exception {

                //given
                var request = createFakeTotalOrderPaymentSuccessValid();
                var loginUserInfo = createFakeLoginUserInfo();

                //when & then
                assertThatThrownBy(
                    () -> orderService.createTotalOrderPaymentSuccess(request, loginUserInfo))
                    .isInstanceOf(OrderException.class);
            }
        }
    }
}