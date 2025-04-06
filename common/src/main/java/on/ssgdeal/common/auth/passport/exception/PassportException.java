package on.ssgdeal.common.auth.passport.exception;


import on.ssgdeal.common.global.exception.CustomException;

public class PassportException extends CustomException {

    public PassportException(PassportExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public static class  PassportRetrievalFailedException extends PassportException {
        public PassportRetrievalFailedException() {
            super(PassportExceptionCode.PASSPORT_RETRIEVAL_FAILED);
        }
    }

    public static class PassportValidationFailedException extends PassportException {
        public PassportValidationFailedException() {
            super(PassportExceptionCode.PASSPORT_VALIDATION_FAILED);
        }
    }
}
