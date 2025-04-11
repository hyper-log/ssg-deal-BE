package on.ssgdeal.payment_service.infrastructure.client.TossPaymentClient;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@ConfigurationProperties(prefix = "payment")
public class PaymentProperties {
    private String secretKey;
    private String baseUrl;
    private String confirmEndpoint;
    private String cancelEndpoint;

    public String getConfirmUrl() {
        return baseUrl + confirmEndpoint;
    }

    public String getCancelUrl(String paymentKey) {
        return String.format(baseUrl + cancelEndpoint, paymentKey);
    }

    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setConfirmEndpoint(String confirmEndpoint) {
        this.confirmEndpoint = confirmEndpoint;
    }

    public void setCancelEndpoint(String cancelEndpoint) {
        this.cancelEndpoint = cancelEndpoint;
    }
}
