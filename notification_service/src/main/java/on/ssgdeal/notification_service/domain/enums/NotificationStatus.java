package on.ssgdeal.notification_service.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationStatus {
    PENDING("전송 중"),
    SUCCESS("전송 성공"),
    FAILED("전송 실패"),
    ;
    private final String description;
}
