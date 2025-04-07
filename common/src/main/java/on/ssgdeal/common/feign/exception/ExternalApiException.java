package on.ssgdeal.common.feign.exception;


import on.ssgdeal.common.global.exception.CustomException;

public class ExternalApiException extends CustomException {

    public ExternalApiException(ExternalApiExceptionCode e) {
        super(e);
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

    public static class ExternalApiDefaultException extends ExternalApiException {

        public ExternalApiDefaultException() {
            super(ExternalApiExceptionCode.DEFAULT);
        }
    }
}
