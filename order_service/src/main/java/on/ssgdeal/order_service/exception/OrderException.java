package on.ssgdeal.order_service.exception;

import on.ssgdeal.common.global.exception.CustomException;
import on.ssgdeal.common.global.exception.ExceptionCode;

public class OrderException extends CustomException {

    public OrderException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
