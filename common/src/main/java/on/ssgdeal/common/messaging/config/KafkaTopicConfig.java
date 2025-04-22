package on.ssgdeal.common.messaging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;

@Configuration
@EnableKafka
public class KafkaTopicConfig {

    @Bean
    public NewTopics newTopics() {
        return new NewTopics(
        );
    }
}
