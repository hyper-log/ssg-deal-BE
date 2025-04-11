package on.ssgdeal.payment_service.infrastructure.client.TossPaymentClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.payment_service.configuration.PaymentExceptionInterceptor;
import on.ssgdeal.payment_service.configuration.PaymentLoggingInterceptor;
import on.ssgdeal.payment_service.domain.enums.PaymentFailReason;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentConfirmException;
import on.ssgdeal.payment_service.infrastructure.client.TossPaymentClient.dto.request.PaymentConfirmRequestDto;
import on.ssgdeal.payment_service.infrastructure.client.TossPaymentClient.dto.response.PaymentConfirmResponseDto;
import on.ssgdeal.payment_service.infrastructure.client.TossPaymentClient.dto.response.PaymentFailResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class PaymentClient {

    private static final String BASIC_DELIMITER = ":";
    private static final String AUTH_HEADER_PREFIX = "Basic ";
    private static final int CONNECT_TIMEOUT_SECONDS = 1;
    private static final int READ_TIMEOUT_SECONDS = 30;

    private final ObjectMapper objectMapper;
    private final PaymentProperties paymentProperties;
    private final RestClient restClient;

    public PaymentClient(PaymentProperties paymentProperties,
        ObjectMapper objectMapper) {
        this.paymentProperties = paymentProperties;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder()
            .requestFactory(createPaymentRequestFactory())
            .requestInterceptor(new PaymentExceptionInterceptor())
            .requestInterceptor(new PaymentLoggingInterceptor())
            .defaultHeader(HttpHeaders.AUTHORIZATION, createPaymentAuthHeader(paymentProperties))
            .build();
    }

    private ClientHttpRequestFactory createPaymentRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(CONNECT_TIMEOUT_SECONDS).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(READ_TIMEOUT_SECONDS).toMillis());
        return factory;
    }

    private String createPaymentAuthHeader(PaymentProperties paymentProperties) {
        byte[] encodedBytes = Base64.getEncoder()
            .encode((paymentProperties.getSecretKey() + BASIC_DELIMITER).getBytes(
                StandardCharsets.UTF_8));
        return AUTH_HEADER_PREFIX + new String(encodedBytes);
    }

    public PaymentConfirmResponseDto confirmPayment(PaymentConfirmRequestDto requestDto) {
        return restClient.method(HttpMethod.POST)
            .uri(paymentProperties.getConfirmUrl())
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestDto)
            .retrieve()
            .onStatus(HttpStatusCode::isError, (request, response) -> {
                try (InputStream bodyStream = response.getBody()) {
                    String errorBody = new String(bodyStream.readAllBytes(),
                        StandardCharsets.UTF_8);
                    log.error("결제 실패 - 응답 바디: {}", errorBody);

                    PaymentFailReason failReason = extractFailReason(errorBody);
                    throw new PaymentConfirmException(failReason);
                } catch (IOException e) {
                    log.error("응답 바디 읽기 실패", e);
                    throw new PaymentConfirmException(PaymentFailReason.UNKNOWN);
                }
            })
            .body(PaymentConfirmResponseDto.class);
    }

    private PaymentFailReason extractFailReason(String errorBody) {
        try {
            PaymentFailResponseDto failOutput = objectMapper.readValue(errorBody,
                PaymentFailResponseDto.class);
            return PaymentFailReason.fromErrorCode(failOutput.code());
        } catch (IOException e) {
            return PaymentFailReason.UNKNOWN;
        }
    }

}
