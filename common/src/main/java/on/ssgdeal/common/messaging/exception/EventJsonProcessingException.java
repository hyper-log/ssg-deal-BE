package on.ssgdeal.common.messaging.exception;

import on.ssgdeal.common.global.exception.CustomException;

public class EventJsonProcessingException extends CustomException {

    public EventJsonProcessingException(MessagingExceptionCode e) {
        super(e);
    }

    public EventJsonProcessingException() {
        super(MessagingExceptionCode.EVENT_JSON_PROCESSING_EXCEPTION);
    }
}
