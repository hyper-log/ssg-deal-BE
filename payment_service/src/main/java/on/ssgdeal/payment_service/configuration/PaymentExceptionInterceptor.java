package on.ssgdeal.payment_service.configuration;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.payment_service.domain.enums.PaymentFailReason;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentConfirmException;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentTimeoutException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

@Slf4j
public class PaymentExceptionInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
        ClientHttpRequestExecution execution) {
        try {
            return execution.execute(request, body);
        } catch (IOException e) {
            throw new PaymentTimeoutException();
        } catch (Exception e) {
            log.warn("결제 요청 중 예외 발생: {}", e.getMessage());
            throw new PaymentConfirmException(PaymentFailReason.PAYMENT_DECLINED);
        }
    }
}
