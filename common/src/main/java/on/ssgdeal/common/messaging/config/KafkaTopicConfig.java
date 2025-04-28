package on.ssgdeal.common.messaging.config;

import static on.ssgdeal.common.messaging.config.KafkaConsumerConfig.DLT_SUFFIX;

import on.ssgdeal.common.messaging.domain.enums.Topic;
import org.apache.kafka.clients.admin.NewTopic;
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
            increaseStock(),
            orderSuccessNotification(),
            increaseStockDLT(),
            orderSuccessNotificationDLT()
        );
    }

    private NewTopic increaseStock() {
        return new NewTopic(Topic.INCREASE_STOCK_EVENT, 1, (short) 1);
    }

    private NewTopic orderSuccessNotification() {
        return new NewTopic(Topic.ORDER_SUCCESS_NOTIFICATION_EVENT, 1, (short) 1);
    }

    private NewTopic increaseStockDLT() {
        return new NewTopic(getDlt(Topic.INCREASE_STOCK_EVENT), 1, (short) 1);
    }

    private NewTopic orderSuccessNotificationDLT() {
        return new NewTopic(getDlt(Topic.ORDER_SUCCESS_NOTIFICATION_EVENT), 1, (short) 1);
    }

    private String getDlt(String topic) {
        return topic + DLT_SUFFIX;
    }
}
