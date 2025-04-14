package on.ssgdeal.notification_service.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationTemplateType {
    FIND_ID("아이디 찾기"),
    FIND_PASSWORD("비밀번호 찾기"),
    ORDER_COMPLETED("주문 완료"),
    ;
    private final String description;
}
