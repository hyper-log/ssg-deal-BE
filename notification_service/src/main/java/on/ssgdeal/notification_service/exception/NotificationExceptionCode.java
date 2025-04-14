package on.ssgdeal.notification_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotificationExceptionCode implements ExceptionCode {

    NOTIFICATION_TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "알림 템플릿을 찾을 수 없습니다."),
    SLACK_USER_LOOKUP_FAILED(HttpStatus.BAD_REQUEST, "슬랙 유저 ID 조회에 실패했습니다."),
    SLACK_CHANNEL_OPEN_FAILED(HttpStatus.BAD_REQUEST, "슬랙 유저 채널 오픈에 실패했습니다."),
    SLACK_MESSAGE_SEND_FAILED(HttpStatus.BAD_REQUEST, "슬랙 메시지 전송에 실패했습니다."),
    SLACK_MESSAGE_API_FAILED(HttpStatus.BAD_REQUEST, "슬랙 메시지 API가 실패했습니다."),
    INVALID_NUMBER_FORMAT(HttpStatus.BAD_REQUEST, "숫자 변환에 실패했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

