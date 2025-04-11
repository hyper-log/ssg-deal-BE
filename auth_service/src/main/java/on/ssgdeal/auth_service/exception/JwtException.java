package on.ssgdeal.auth_service.exception;


import on.ssgdeal.common.global.exception.CustomException;
import on.ssgdeal.common.global.exception.ExceptionCode;

public class JwtException extends CustomException {

    public JwtException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

}
