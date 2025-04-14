package on.ssgdeal.notification_service.infrastructure.client.slack.dto;

import lombok.Getter;

public record SlackUserInfoResponse(
        boolean ok,
        String userId,
        String teamId
) {
}
