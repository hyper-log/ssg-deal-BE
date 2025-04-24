package on.ssgdeal.payment_service.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import java.util.Optional;
import on.ssgdeal.payment_service.application.fixture.FixtureFactory;
import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentPartialCancelRequestDto;
import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentRequestDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentCancelResponseDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentPartialCancelResponseDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentResponseDto;
import on.ssgdeal.payment_service.domain.entity.Payment;
import on.ssgdeal.payment_service.domain.enums.PaymentFailReason;
import on.ssgdeal.payment_service.domain.enums.PaymentResult;
import on.ssgdeal.payment_service.domain.enums.PaymentStatus;
import on.ssgdeal.payment_service.domain.enums.PaymentType;
import on.ssgdeal.payment_service.domain.repository.PaymentRepository;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentCancelException;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentConfirmException;
import on.ssgdeal.payment_service.infrastructure.client.PaymentClient.dto.response.PaymentConfirmResponseDto;
import on.ssgdeal.payment_service.infrastructure.client.PaymentClient.strategy.PaymentStrategy;
import on.ssgdeal.payment_service.infrastructure.client.PaymentClient.strategy.PaymentStrategyFactory;
import on.ssgdeal.payment_service.infrastructure.client.order.feign.dto.GetValidTotalOrderId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentProcessorServiceImplTest {

    @InjectMocks
    private PaymentProcessorServiceImpl paymentProcessorService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private OrderService orderService;

    @Mock
    private PaymentStrategyFactory paymentStrategyFactory;

    @Mock
    private PaymentStrategy paymentStrategy;

    @Nested
    @DisplayName("결제 승인 요청 테스트")
    class OrderPaymentTests {

        @Test
        @DisplayName("결제 승인 성공 테스트")
        void 결제_승인_성공_테스트() {
            // given
            OrderPaymentRequestDto requestDto = FixtureFactory.getOrderPaymentRequestDto();
            Payment savedPayment = FixtureFactory.getPayment(requestDto, 1L);
            Payment managedPayment = FixtureFactory.getPayment(requestDto, 1L);
            PaymentConfirmResponseDto confirmResponse = FixtureFactory.getPaymentConfirmResponseDto(
                requestDto);

            given(
                paymentStrategyFactory.getStrategy(PaymentType.TOSS)).willReturn(
                paymentStrategy);
            given(paymentService.savePayment(requestDto)).willReturn(savedPayment);
            given(paymentRepository.findById(savedPayment.getId())).willReturn(
                Optional.of(managedPayment));
            given(orderService.getValidTotalOrderId(savedPayment.getTotalOrderId()))
                .willReturn(new GetValidTotalOrderId(true));
            given(paymentStrategy.confirm(any())).willReturn(confirmResponse);

            // when
            OrderPaymentResponseDto responseDto = paymentProcessorService.orderPayment(requestDto);

            // then
            assertThat(responseDto.paymentStatus()).isEqualTo("COMPLETED");
            assertThat(responseDto.paymentKey()).isEqualTo(confirmResponse.paymentKey());
            assertThat(managedPayment.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
        }

        @Test
        @DisplayName("결제 승인 실패 테스트")
        void 결제_승인_실패_테스트() {
            // given
            OrderPaymentRequestDto requestDto = FixtureFactory.getOrderPaymentRequestDto();
            Payment savedPayment = FixtureFactory.getPayment(requestDto, 1L);
            Payment managedPayment = FixtureFactory.getPayment(requestDto, 1L);

            given(
                paymentStrategyFactory.getStrategy(PaymentType.TOSS)).willReturn(
                paymentStrategy);
            given(paymentService.savePayment(requestDto)).willReturn(savedPayment);
            given(paymentRepository.findById(savedPayment.getId())).willReturn(
                Optional.of(managedPayment));
            given(orderService.getValidTotalOrderId(savedPayment.getTotalOrderId()))
                .willReturn(new GetValidTotalOrderId(true));
            given(paymentStrategy.confirm(any()))
                .willThrow(new PaymentConfirmException(PaymentFailReason.INVALID_REQUEST));

            // when
            OrderPaymentResponseDto responseDto = paymentProcessorService.orderPayment(requestDto);

            // then
            assertThat(responseDto.paymentStatus()).isEqualTo("FAILED");
            assertThat(responseDto.paymentFailReason()).isEqualTo("INVALID_REQUEST");
            assertThat(managedPayment.getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
            assertThat(managedPayment.getPaymentFailReason()).isEqualTo(
                PaymentFailReason.INVALID_REQUEST);
        }
    }

    @Nested
    @DisplayName("결제 취소 요청 테스트")
    class OrderPaymentCancelTests {

        @Test
        @DisplayName("결제 전체 취소 성공 테스트")
        void 결제_취소_성공_테스트() {
            // given
            Long totalOrderId = 123L;
            Payment payment = FixtureFactory.getPayment();

            given(
                paymentStrategyFactory.getStrategy(PaymentType.TOSS)).willReturn(
                paymentStrategy);
            given(orderService.getValidTotalOrderId(totalOrderId))
                .willReturn(new GetValidTotalOrderId(true));
            given(paymentService.getPaymentByTotalOrderId(totalOrderId)).willReturn(payment);

            // when
            OrderPaymentCancelResponseDto responseDto = paymentProcessorService.orderPaymentCancel(
                totalOrderId);

            // then
            assertThat(responseDto.result()).isEqualTo(String.valueOf(PaymentResult.SUCCESS));
            assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.CANCELED);
        }

        @Test
        @DisplayName("결제 전체 취소 실패 테스트")
        void 결제_취소_실패_테스트() {
            // given
            Long totalOrderId = 123L;
            Payment payment = FixtureFactory.getPayment();

            given(
                paymentStrategyFactory.getStrategy(PaymentType.TOSS)).willReturn(
                paymentStrategy);
            given(orderService.getValidTotalOrderId(totalOrderId))
                .willReturn(new GetValidTotalOrderId(true));
            given(paymentService.getPaymentByTotalOrderId(totalOrderId)).willReturn(payment);
            willThrow(new PaymentCancelException()).given(paymentStrategy).cancel(payment);

            // when
            OrderPaymentCancelResponseDto responseDto = paymentProcessorService.orderPaymentCancel(
                totalOrderId);

            // then
            assertThat(responseDto.result()).isEqualTo(String.valueOf(PaymentResult.FAIL));
            assertThat(payment.getPaymentStatus()).isNotEqualTo(PaymentStatus.CANCELED);
        }

        @Test
        @DisplayName("결제 부분 취소 성공 테스트")
        void 결제_부분_취소_성공_테스트() {
            // given
            Long totalOrderId = 123L;
            Payment payment = FixtureFactory.getPayment();
            OrderPaymentPartialCancelRequestDto requestDto = new OrderPaymentPartialCancelRequestDto(
                500L);

            given(
                paymentStrategyFactory.getStrategy(PaymentType.TOSS)).willReturn(
                paymentStrategy);
            given(orderService.getValidTotalOrderId(totalOrderId))
                .willReturn(new GetValidTotalOrderId(true));
            given(paymentService.getPaymentByTotalOrderId(totalOrderId)).willReturn(payment);

            // when
            OrderPaymentPartialCancelResponseDto responseDto =
                paymentProcessorService.orderPaymentPartialCancel(totalOrderId, requestDto);

            // then
            assertThat(responseDto.result()).isEqualTo(String.valueOf(PaymentResult.SUCCESS));
            assertThat(payment.getPaymentStatus()).isEqualTo(
                PaymentStatus.CANCELED);
        }

        @Test
        @DisplayName("결제 부분 취소 실패 테스트")
        void 결제_부분_취소_실패_테스트() {
            // given
            Long totalOrderId = 123L;
            Payment payment = FixtureFactory.getPayment();
            OrderPaymentPartialCancelRequestDto requestDto = new OrderPaymentPartialCancelRequestDto(
                500L);

            given(
                paymentStrategyFactory.getStrategy(PaymentType.TOSS)).willReturn(
                paymentStrategy);
            given(orderService.getValidTotalOrderId(totalOrderId))
                .willReturn(new GetValidTotalOrderId(true));
            given(paymentService.getPaymentByTotalOrderId(totalOrderId)).willReturn(payment);

            willThrow(new PaymentCancelException()).given(paymentStrategy)
                .partialCancel(payment, requestDto);

            // when
            OrderPaymentPartialCancelResponseDto responseDto =
                paymentProcessorService.orderPaymentPartialCancel(totalOrderId, requestDto);

            // then
            assertThat(responseDto.result()).isEqualTo(String.valueOf(PaymentResult.FAIL));
            assertThat(payment.getPaymentStatus()).isNotEqualTo(PaymentStatus.CANCELED);
        }
    }
}