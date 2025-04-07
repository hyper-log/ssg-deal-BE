package on.ssgdeal.gateway_service.exception;


public class ApiGatewayException extends CustomException {

    public ApiGatewayException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

}
