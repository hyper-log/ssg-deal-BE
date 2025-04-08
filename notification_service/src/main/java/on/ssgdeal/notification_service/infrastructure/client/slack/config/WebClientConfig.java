package on.ssgdeal.notification_service.infrastructure.client.slack.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${slack.bot.token}")
    private String slackBotToken;

    @Value("${slack.base-url}")
    private String baseUrl;

    @Bean
    public WebClient slackWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + slackBotToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

}
