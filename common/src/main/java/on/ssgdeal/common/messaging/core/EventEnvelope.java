package on.ssgdeal.common.messaging.core;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.messaging.domain.enums.Topic;
import on.ssgdeal.common.messaging.exception.NonRecoverableException;

@Slf4j
public record EventEnvelope<T extends EventPayload>(
    String topic,
    T payload,
    String timestamp
) {

    public static <T extends EventPayload> EventEnvelope<T> wrap(
        String topic, T payload, String timestamp
    ) {
        return new EventEnvelope<>(topic, payload, timestamp);
    }

    public static <T extends EventPayload> EventEnvelope<T> wrap(
        String topic, T payload
    ) {
        return new EventEnvelope<>(topic, payload, LocalDateTime.now().toString());
    }

    public static <T extends EventPayload> T unwrap(EventEnvelope<T> envelope) {
        return envelope.payload;
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("EventEnvelope 의 직렬화를 실패했습니다.");
            throw new NonRecoverableException();
        }
    }

    public static <T extends EventPayload> EventEnvelope<T> fromJson(String json, Class<T> tClass) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(json);
            log.info("Envelope Json 역직렬화 시작 - jsonNode: {}", jsonNode);

            String topic = jsonNode.get("topic").asText();
            String timestamp = jsonNode.get("timestamp").asText();
            T payload = mapper.treeToValue(jsonNode.get("payload"), tClass);

            return EventEnvelope.wrap(topic, payload, timestamp);
        } catch (Exception e) {
            log.error("Envelope JSON 역직렬화 실패", e);
            throw new NonRecoverableException();
        }
    }
}
