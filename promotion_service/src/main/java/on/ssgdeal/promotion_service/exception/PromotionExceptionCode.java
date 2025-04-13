package on.ssgdeal.promotion_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PromotionExceptionCode implements ExceptionCode {

    PROMOTION_NOT_FOUND(HttpStatus.NOT_FOUND, "프로모션을 찾을 수 없습니다."),
    PROMOTION_NOT_FINISHED(HttpStatus.BAD_REQUEST, "종료되지 않은 프로모션입니다."),
    PROMOTION_NOT_IN_PROGRESS(HttpStatus.BAD_REQUEST, "진행 중인 프로모션이 아닙니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
