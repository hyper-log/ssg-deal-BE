package on.ssgdeal.user_service.exception;


import on.ssgdeal.common.global.exception.CustomException;
import on.ssgdeal.common.global.exception.ExceptionCode;

public class UserException extends CustomException {

    public UserException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public static class UserSlackEmailIsNullException extends UserException {

        public UserSlackEmailIsNullException() {
            super(UserExceptionCode.USER_SLACK_EMAIL_IS_NULL);
        }
    }

    public static class UserSlackEmailInvalidFormatException extends UserException {

        public UserSlackEmailInvalidFormatException() {
            super(UserExceptionCode.USER_SLACK_EMAIL_INVALID_FORMAT);
        }
    }

    public static class UserIsNotAllowedException extends UserException {

        public UserIsNotAllowedException() {
            super(UserExceptionCode.USER_IS_NOT_ALLOWED);
        }
    }

    public static class UserNotFoundException extends UserException {

        public UserNotFoundException() {
            super(UserExceptionCode.USER_NOT_FOUND);
        }
    }

    public static class UserAlreadyExistsException extends UserException {

        public UserAlreadyExistsException() {
            super(UserExceptionCode.USER_ALREADY_EXISTS);
        }
    }

    public static class UserUnauthorizedException extends UserException {

        public UserUnauthorizedException() {
            super(UserExceptionCode.UNAUTHORIZED);
        }
    }

    public static class UserSlackEmailAlreadyExistsException extends UserException {

        public UserSlackEmailAlreadyExistsException() {
            super(UserExceptionCode.USER_SLACK_EMAIL_ALREADY_EXISTS);
        }
    }

    public static class UserNicknameIsNullException extends UserException {

        public UserNicknameIsNullException() {
            super(UserExceptionCode.USER_NICKNAME_IS_NULL);
        }
    }
}
