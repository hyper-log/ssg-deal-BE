package on.ssgdeal.notification_service.infrastructure.client.slack;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.notification_service.application.service.SlackClient;
import on.ssgdeal.notification_service.exception.NotificationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j(topic = "SlackClientImpl")
@Component
@RestController
@RequiredArgsConstructor
public class SlackClientImpl implements SlackClient {

    private final WebClient slackWebClient;
    private final static String LOOKUP_BY_SLACK_EMAIL_URL = "/users.lookupByEmail";
    private final static String GET_CHANNEL_ID_BY_SLACK_USER_ID_URL = "/conversations.open";
    private final static String POST_SLACK_NOTIFICATION_URL = "/chat.postMessage";

    /**
     * 슬랙 이메일로 슬랙 유저 ID 조회
     */
    public String getSlackIdByEmail(String email) {

        JsonNode response = slackWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(LOOKUP_BY_SLACK_EMAIL_URL)
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (response != null && response.get("ok").asBoolean()) {
            return response.get("user").get("id").asText();
        } else {
            log.warn("슬랙 유저 ID 조회를 실패했습니다.");
            throw new NotificationException.SlackUserLookupFailedException();
        }
    }

    /**
     * 슬랙 유저 ID 채널 ID 조회
     */
    public String getChannelIdByUserId(String userId) {
        JsonNode response = slackWebClient.post()
                .uri(GET_CHANNEL_ID_BY_SLACK_USER_ID_URL)
                .bodyValue(Map.of("users", userId))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (response != null && response.get("ok").asBoolean()) {
            return response.get("channel").get("id").asText();
        } else {
            log.warn("슬랙 유저 ID 조회를 실패했습니다.");
            throw new NotificationException.SlackChannelOpenFailedException();
        }
    }

    /**
     * 메시지 전송
     */
    public String sendNotificationToUser(String email, String content) {
        try {
            String userId = getSlackIdByEmail(email);
            String channelId = getChannelIdByUserId(userId);

            Map<String, Object> requestBody = Map.of(
                    "channel", channelId,
                    "text", content
            );

            JsonNode response = slackWebClient.post()
                    .uri(POST_SLACK_NOTIFICATION_URL)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .doOnNext(body -> log.info("슬랙 메세지 전송 응답: {}", body))
                    .block();

            if (response != null && response.get("ok").asBoolean()) {
                return response.get("ts").asText();
            } else {
                log.warn("슬랙 API가 메시지 전송을 실패했습니다.");
                throw new NotificationException.SlackMessageSendFailedException();
            }
        } catch (Exception e) {
            throw new NotificationException.SlackApiErrorException();
        }
    }
}
