package on.ssgdeal.common.feign.exception;


import on.ssgdeal.common.global.exception.CustomException;

public class ExternalApiException extends CustomException {

    public ExternalApiException(ExternalApiExceptionCode e) {
        super(e);
    }

    public static class ExternalApiNotFoundException extends ExternalApiException {

        public ExternalApiNotFoundException() {
            super(ExternalApiExceptionCode.NOT_FOUND_RESPONSE);
        }
    }

    public static class ExternalApiBadRequestException extends ExternalApiException {

        public ExternalApiBadRequestException() {
            super(ExternalApiExceptionCode.BAD_REQUEST_RESPONSE);
        }
    }

    public static class ExternalApiClientException extends ExternalApiException {

        public ExternalApiClientException() {
            super(ExternalApiExceptionCode.CLIENT_ERROR_RESPONSE);
        }

    }

    public static class ExternalApiServerException extends ExternalApiException {

        public ExternalApiServerException() {
            super(ExternalApiExceptionCode.SERVER_ERROR_RESPONSE);
        }
    }

    public static class WrongResponseTypeApiException extends ExternalApiException {

        public WrongResponseTypeApiException() {
            super(ExternalApiExceptionCode.WRONG_RESPONSE_TYPE);
        }
    }

    public static class ExternalApiDefaultException extends ExternalApiException {

        public ExternalApiDefaultException() {
            super(ExternalApiExceptionCode.DEFAULT);
        }
    }
}
