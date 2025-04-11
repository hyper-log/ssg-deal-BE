package on.ssgdeal.common.global.exception;

public class CommonException extends CustomException {

    public CommonException(CommonExceptionCode e) {
        super(e);
    }

    public static class CommonRequestNotFoundException extends CommonException {

        public CommonRequestNotFoundException() {
            super(CommonExceptionCode.REQUEST_NOT_FOUND);
        }
    }

    public static class CommonForbiddenException extends CommonException {

        public CommonForbiddenException() {
            super(CommonExceptionCode.FORBIDDEN);
        }
    }

    public static class CommonPassportNotFoundException extends CommonException {

        public CommonPassportNotFoundException() {
            super(CommonExceptionCode.PASSPORT_NOT_FOUND);
        }
    }
}