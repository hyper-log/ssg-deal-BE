package on.ssgdeal.order_service.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.messaging.domain.entity.EventPayload;
import on.ssgdeal.order_service.infrastructure.messaging.dtos.IncreaseStockEvent;
import on.ssgdeal.order_service.infrastructure.messaging.dtos.OrderSuccessNotificationEvent;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {

    ORDER_SUCCESS_NOTIFICATION_EVENT(OrderSuccessNotificationEvent.class,
        Topic.ORDER_SUCCESS_NOTIFICATION_EVENT),
    INCREASE_STOCK_EVENT(IncreaseStockEvent.class,
        Topic.INCREASE_STOCK_EVENT),
    ;
    private final Class<? extends EventPayload> payloadClass;
    private final String topic;

    public static EventType from(String type) {
        try {
            return valueOf(type);
        } catch (Exception e) {
            log.error("[EventType.from] type={} is invalid",
                type,
                e);
            return null;
        }
    }

    public static class Topic {

        public static final String ORDER_SUCCESS_NOTIFICATION_EVENT = "ssgdeal.orderSuccessNotificationEvent";
        public static final String INCREASE_STOCK_EVENT = "ssgdeal.increaseStockEvent";
    }
}
