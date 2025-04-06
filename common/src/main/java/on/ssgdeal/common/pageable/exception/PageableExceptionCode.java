package on.ssgdeal.common.pageable.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PageableExceptionCode implements ExceptionCode {

    INVALID_PAGE_SIZE(HttpStatus.BAD_REQUEST, "잘못된 페이지 사이즈입니다."),
    INVALID_SORT_BY(HttpStatus.BAD_REQUEST, "잘못된 정렬 기준입니다."),
    INVALID_PAGE_NUMBER(HttpStatus.BAD_REQUEST, "잘못된 페이지 번호입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
