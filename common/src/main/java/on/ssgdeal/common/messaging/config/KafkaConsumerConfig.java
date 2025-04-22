package on.ssgdeal.common.messaging.config;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import on.ssgdeal.common.messaging.exception.NonRecoverableException;
import on.ssgdeal.common.messaging.producer.KafkaOutboxProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configs = getConsumerCommonConfigs();

        return new DefaultKafkaConsumerFactory<>(
            configs, new StringDeserializer(), new StringDeserializer());
    }

    private Map<String, Object> getConsumerCommonConfigs() {
        Map<String, Object> configs = new HashMap<>();

        // kafka 서버
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // 컨슈머 그룹 내 여러 컨슈머의 메시지 중복 처리를 방지
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "on-ssgdeal-group");

        // key 역직렬화
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // value 역직렬화
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // 자동 토픽 생성
        configs.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
        // offset 수동 커밋
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // 오프셋 초기화 시점
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // consumer offset 커밋 트랜잭션 격리 수준
        configs.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        // 리밸런싱 발생 시 컨슈머의 파티션 재분배 전략
        // CooperativeStickyAssignor => 컨슈머들은 기존 파티션을 최대한 유지
        configs.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
            CooperativeStickyAssignor.class);

        // 세션 타임아웃 (Default)
        configs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 45000);
        // API 타임아웃 (Default)
        configs.put(ConsumerConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, 60000);
        // 요청 타임아웃 (Default)
        configs.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        // 컨슈머 헬스체크 (Default / 3)
        configs.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 1000);

        // poll() 시 가져올 최대 레코드 수 (Default)
        configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        // poll() 간격 최대 시간 (Default)
        configs.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 50000);

        // 재연결 backoff 기본 ms (Default)
        configs.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, 50);
        // 재연결 backoff 최대 ms (Default)
        configs.put(ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, 1000);
        // 재시도 backoff 기본 ms (Default)
        configs.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 100);
        // 재시도 backoff 최대 ms (Default)
        configs.put(ConsumerConfig.RETRY_BACKOFF_MAX_MS_CONFIG, 1000);

        return configs;
    }

    @Bean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
        DefaultErrorHandler defaultErrorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        // 중복 방지를 위해 메시지 처리 완료시 즉시 offset 커밋
        factory.getContainerProperties()
            .setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        // DLQ 정책 및 재시도 정책 적용
        factory.setCommonErrorHandler(defaultErrorHandler);
        // 컨테이너 동시성 설정
        factory.setConcurrency(1);

        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = getDeadLetterPublishingRecoverer(kafkaTemplate);

        // 최대 반복 3번의 재시도 및 간격 지수 증가
        ExponentialBackOffWithMaxRetries backoff = new ExponentialBackOffWithMaxRetries(3);
        backoff.setInitialInterval(2000); // 2초
        backoff.setMultiplier(2.0);       // 2배씩 증가
        backoff.setMaxInterval(10000);    // 최대 10초

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backoff);

        // IllegalArgumentException 은 재시도 하지 않고 바로 DLQ 로 보냄
        errorHandler.addNotRetryableExceptions(
            IllegalArgumentException.class,
            NonRecoverableException.class
        );

        return errorHandler;
    }

    private DeadLetterPublishingRecoverer getDeadLetterPublishingRecoverer(
        KafkaTemplate<String, String> kafkaTemplate
    ) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
            kafkaTemplate,
            // 기존 파티션과 DLQ 파티션의 차이가 존재할 수 있으므로 파티션 0 고정
            (cr, ex) -> new TopicPartition(cr.topic() + KafkaOutboxProducer.DLT_SUFFIX, 0)
        );
        recoverer.setHeadersFunction((record, ex) -> {
            record.headers().add(
                "Kafka_DLT_Reason", ex.getMessage().getBytes(StandardCharsets.UTF_8));
            record.headers().add(
                "Kafka_OriginalTimestamp", String.valueOf(record.timestamp()).getBytes());
            return record.headers();
        });
        return recoverer;
    }
}
