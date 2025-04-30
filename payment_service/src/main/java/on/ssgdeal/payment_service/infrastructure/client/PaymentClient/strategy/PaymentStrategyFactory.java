package on.ssgdeal.payment_service.infrastructure.client.PaymentClient.strategy;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import on.ssgdeal.payment_service.domain.enums.PaymentType;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentUnsupportedTypeException;
import org.springframework.stereotype.Component;

@Component
public class PaymentStrategyFactory {

    private final Map<PaymentType, PaymentStrategy> strategies;

    public PaymentStrategyFactory(Map<String, PaymentStrategy> strategyMap) {
        this.strategies = new EnumMap<>(PaymentType.class);

        for (Map.Entry<String, PaymentStrategy> entry : strategyMap.entrySet()) {
            PaymentType type = PaymentType.valueOf(entry.getKey());
            strategies.put(type, entry.getValue());
        }
    }

    public PaymentStrategy getStrategy(PaymentType type) {
        return Optional.ofNullable(strategies.get(type))
            .orElseThrow(PaymentUnsupportedTypeException::new);
    }
}
