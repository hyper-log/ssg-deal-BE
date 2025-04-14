package on.ssgdeal.user_service.exception.destination;

import on.ssgdeal.common.global.exception.CustomException;
import on.ssgdeal.common.global.exception.ExceptionCode;

public class DestinationException extends CustomException {

    public DestinationException(ExceptionCode e) {
        super(e);
    }

    public static class DestinationNotFoundException extends DestinationException {

        public DestinationNotFoundException() {
            super(DestinationExceptionCode.DESTINATION_NOT_FOUND);
        }
    }

    public static class DestinationUnauthorizedException extends DestinationException {

        public DestinationUnauthorizedException() {
            super(DestinationExceptionCode.UNAUTHORIZED);
        }
    }

    public static class DestinationAddressIsNullException extends DestinationException {

        public DestinationAddressIsNullException() {
            super(DestinationExceptionCode.DESTINATION_ADDRESS_IS_NULL);
        }
    }

}
