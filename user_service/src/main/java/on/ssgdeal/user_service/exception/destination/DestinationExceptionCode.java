package on.ssgdeal.user_service.exception.destination;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DestinationExceptionCode implements ExceptionCode {
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "조회 권한이 없습니다."),
    DESTINATION_NOT_FOUND(HttpStatus.NOT_FOUND, "목적지 정보가 없습니다."),
    DESTINATION_ADDRESS_IS_NULL(HttpStatus.BAD_REQUEST, "주소가 입력되지 않았습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
