package on.ssgdeal.auth_service.exception;


import on.ssgdeal.common.global.exception.CustomException;
import on.ssgdeal.common.global.exception.ExceptionCode;

public class HashException extends CustomException {

    public HashException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

}
