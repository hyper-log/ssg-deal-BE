package on.ssgdeal.cart_service.exception;

import on.ssgdeal.common.global.exception.CustomException;

public class CartException extends CustomException {

    public CartException(CartExceptionCode e) {
        super(e);
    }

    public static class CartProductNotFoundException extends CustomException {
        public CartProductNotFoundException() {
            super(CartExceptionCode.CART_PRODUCT_NOT_FOUND);
        }
    }

    public static class NotEnoughStockException extends CartException {
        public NotEnoughStockException() {
            super(CartExceptionCode.NOT_ENOUGH_STOCK);
        }
    }

    public static class MustBePositiveQuantityException extends CartException {
        public MustBePositiveQuantityException() {
            super(CartExceptionCode.MUST_BE_POSITIVE_QUANTITY);
        }
    }
}
