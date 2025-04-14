package on.ssgdeal.notification_service.infrastructure.client.slack.converter;

import on.ssgdeal.notification_service.exception.NotificationException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class SlackTimestampToKSTConverter {
    public LocalDateTime convertToKST(String slackTimestamp) {
        try {
            double timestampDouble = Double.parseDouble(slackTimestamp);
            long seconds = (long) timestampDouble;
            long nanos = (long) ((timestampDouble - seconds) * 1_000_000_000); // 마이크로초를 나노초로 변환
            Instant instant = Instant.ofEpochSecond(seconds, nanos);
            ZoneId kstZoneId = ZoneId.of("Asia/Seoul");
            return LocalDateTime.ofInstant(instant, kstZoneId);
        } catch (NumberFormatException e) {
            throw new NotificationException.InvalidNumberFormatException();
        }
    }
}
