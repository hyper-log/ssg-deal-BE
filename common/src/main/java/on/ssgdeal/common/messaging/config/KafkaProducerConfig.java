package on.ssgdeal.common.messaging.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        ProducerFactory<String, String> producerFactory = producerFactory();

        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configs = getProducerCommonConfigs();

        return new DefaultKafkaProducerFactory<>(configs);
    }

    private Map<String, Object> getProducerCommonConfigs() {
        Map<String, Object> configs = new HashMap<>();

        // kafka 서버
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // key 직렬화
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class);
        // value 직렬화
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class);

        //== Exactly-once 핵심 설정
        // 메시지 중복 방지를 위한 멱등성 설정 => 멱등성 활성화 시 acks=all 자동 설정됨 (Kafka 2.1+)
        configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        // 모든 브로커가 메시지를 수신했을 때 응답
        configs.put(ProducerConfig.ACKS_CONFIG, "all");
        // 단일 브로커 연결당 동시 전송 가능한 미확인(unacknowledged) 요청의 최대 개수 ( 멱등성 활성화 필수 )
        configs.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        // 트랜잭션 타임아웃
        configs.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 300000);
        // 재시도 횟수
        configs.put(ProducerConfig.RETRIES_CONFIG, 3);
        // 동일 Producer 재시작 간에도 트랜잭션을 식별하기 위한 ID
        configs.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,
            "tx-ssgdeal-" + applicationName + UUID.randomUUID());

        //== 성능 튜닝
        // batch size (32KB)
        configs.put(ProducerConfig.BATCH_SIZE_CONFIG, 32768);
        // 메시지 발행 지연 시간
        configs.put(ProducerConfig.LINGER_MS_CONFIG, 100);
        // 전송 타임아웃  ( >= 요청 타임아웃 + 발행 지연 시간 )
        configs.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 60000);
        // 요청 타임아웃 (Default)
        configs.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        // 재연결 지연 시간 (Default)
        configs.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, 50);
        // 재시도 지연 시간 (Default)
        configs.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 100);
        // 재시도 최대 지연 시간 (Default)
        configs.put(ProducerConfig.RETRY_BACKOFF_MAX_MS_CONFIG, 1000);

        return configs;
    }
}
