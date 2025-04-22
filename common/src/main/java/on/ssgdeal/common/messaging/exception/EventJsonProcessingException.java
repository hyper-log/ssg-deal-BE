package on.ssgdeal.common.messaging.exception;

public class EventJsonProcessingException extends NonRecoverableException {

    public EventJsonProcessingException(MessagingExceptionCode e) {
        super(e);
    }

    public EventJsonProcessingException() {
        super(MessagingExceptionCode.EVENT_JSON_PROCESSING_EXCEPTION);
    }
}
