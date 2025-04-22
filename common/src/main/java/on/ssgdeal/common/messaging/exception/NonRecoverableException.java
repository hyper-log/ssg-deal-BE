package on.ssgdeal.common.messaging.exception;

import on.ssgdeal.common.global.exception.CustomException;

public class NonRecoverableException extends CustomException {

    public NonRecoverableException(MessagingExceptionCode e) {
        super(e);
    }

    public NonRecoverableException() {
        super(MessagingExceptionCode.NON_RECOVERABLE);
    }
}
