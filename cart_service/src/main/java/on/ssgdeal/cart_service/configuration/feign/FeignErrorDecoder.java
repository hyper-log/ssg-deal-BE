package on.ssgdeal.cart_service.configuration.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import on.ssgdeal.common.feign.exception.ExternalApiException.ExternalApiClientException;
import on.ssgdeal.common.feign.exception.ExternalApiException.ExternalApiDefaultException;
import on.ssgdeal.common.feign.exception.ExternalApiException.ExternalApiServerException;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        int statusCode = response.status();
        return switch (statusCode / 100) {
            case 4 -> getClientException(methodKey, statusCode);
            case 5 -> getServerException(methodKey, statusCode);
            default -> new ExternalApiDefaultException();
        };
    }

    private static Exception getClientException(String methodKey, int statusCode) {
        return switch (statusCode) {
            default -> new ExternalApiClientException();
        };
    }

    private static ExternalApiServerException getServerException(String methodKey, int statusCode) {
        return switch (statusCode) {
            default -> new ExternalApiServerException();
        };
    }
}
