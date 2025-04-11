package on.ssgdeal.auth_service.exception;


import on.ssgdeal.common.global.exception.CustomException;
import on.ssgdeal.common.global.exception.ExceptionCode;

public class AuthException extends CustomException {

    public AuthException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

}
