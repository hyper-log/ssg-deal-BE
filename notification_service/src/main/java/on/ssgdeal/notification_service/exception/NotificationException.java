package on.ssgdeal.notification_service.exception;

import on.ssgdeal.common.global.exception.CustomException;

public class NotificationException extends CustomException {
    public NotificationException(NotificationExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public static class NotificationTemplateNotFoundException extends NotificationException {
        public NotificationTemplateNotFoundException() {
            super(NotificationExceptionCode.NOTIFICATION_TEMPLATE_NOT_FOUND);
        }
    }

    public static class SlackUserLookupFailedException extends NotificationException {
        public SlackUserLookupFailedException() {
            super(NotificationExceptionCode.SLACK_USER_LOOKUP_FAILED);
        }
    }

    public static class SlackChannelOpenFailedException extends NotificationException {
        public SlackChannelOpenFailedException() {
            super(NotificationExceptionCode.SLACK_CHANNEL_OPEN_FAILED);
        }
    }

    public static class SlackMessageSendFailedException extends NotificationException {
        public SlackMessageSendFailedException() {
            super(NotificationExceptionCode.SLACK_MESSAGE_SEND_FAILED);
        }
    }

    public static class SlackApiErrorException extends NotificationException {
        public SlackApiErrorException() {
            super(NotificationExceptionCode.SLACK_MESSAGE_SEND_FAILED);
        }
    }

    public static class InvalidNumberFormatException extends NotificationException {
        public InvalidNumberFormatException() {
            super(NotificationExceptionCode.INVALID_NUMBER_FORMAT);
        }
    }

    public static class NotificationChannelNotFoundException extends NotificationException {
        public NotificationChannelNotFoundException() {
            super(NotificationExceptionCode.NOTIFICATION_CHANNEL_NOT_FOUND);
        }
    }

    public static class NotificationNotFoundException extends NotificationException {
        public NotificationNotFoundException() {
            super(NotificationExceptionCode.NOTIFICATION_NOT_FOUND);
        }
    }

}
