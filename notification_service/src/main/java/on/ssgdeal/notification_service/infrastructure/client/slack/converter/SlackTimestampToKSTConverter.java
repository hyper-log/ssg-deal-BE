package on.ssgdeal.notification_service.infrastructure.client.slack.converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class SlackTimestampToKSTConverter {
    public static LocalDateTime convertToKST(String slackTimestamp) {
        try {
            double timestampDouble = Double.parseDouble(slackTimestamp);
            long seconds = (long) timestampDouble;
            long nanos = (long) ((timestampDouble - seconds) * 1_000_000_000); // 마이크로초를 나노초로 변환
            Instant instant = Instant.ofEpochSecond(seconds, nanos);
            ZoneId kstZoneId = ZoneId.of("Asia/Seoul");
            return LocalDateTime.ofInstant(instant, kstZoneId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
