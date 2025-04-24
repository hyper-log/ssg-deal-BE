package on.ssgdeal.payment_service.infrastructure.client.PaymentClient.strategy;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.payment_service.domain.enums.PaymentType;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentUnsupportedTypeException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyFactory {

    private final Map<PaymentType, PaymentStrategy> strategies;

    public PaymentStrategy getStrategy(PaymentType type) {
        return Optional.ofNullable(strategies.get(type))
            .orElseThrow(PaymentUnsupportedTypeException::new);
    }
}
