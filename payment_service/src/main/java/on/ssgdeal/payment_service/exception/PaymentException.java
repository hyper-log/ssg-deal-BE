package on.ssgdeal.payment_service.exception;

import on.ssgdeal.common.global.exception.CustomException;
import on.ssgdeal.payment_service.domain.enums.PaymentFailReason;

public class PaymentException extends CustomException {

    public PaymentException(PaymentExceptionCode paymentExceptionCode) {
        super(paymentExceptionCode);
    }

    public static class PaymentNotFoundException extends PaymentException {

        public PaymentNotFoundException() {
            super(PaymentExceptionCode.PAYMENT_NOT_FOUND);
        }
    }

    public static class PaymentConfirmException extends PaymentException {

        private final PaymentFailReason failReason;

        public PaymentConfirmException(PaymentFailReason failReason) {
            super(PaymentExceptionCode.PAYMENT_CONFIRM_ERROR);
            this.failReason = failReason;
        }

        public PaymentFailReason getFailReason() {
            return failReason;
        }
    }

    public static class PaymentTimeoutException extends PaymentException {

        public PaymentTimeoutException() {
            super(PaymentExceptionCode.PAYMENT_TIMEOUT);
        }

        public PaymentFailReason getFailReason() {
            return PaymentFailReason.TIMEOUT;
        }
    }
}
