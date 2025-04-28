package on.ssgdeal.common.messaging.exception;

public class EventPayloadException extends NonRecoverableException {

    public EventPayloadException(MessagingExceptionCode e) {
        super(e);
    }

    public static class EventDeserializeException extends EventPayloadException {

        public EventDeserializeException() {
            super(MessagingExceptionCode.EVENT_JSON_DESERIALIZE_EXCEPTION);
        }
    }

    public static class EventSerializeException extends EventPayloadException {

        public EventSerializeException() {
            super(MessagingExceptionCode.EVENT_JSON_SERIALIZE_EXCEPTION);
        }
    }
}
