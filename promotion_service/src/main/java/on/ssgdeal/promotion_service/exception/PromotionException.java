package on.ssgdeal.promotion_service.exception;

import on.ssgdeal.common.global.exception.CustomException;

public class PromotionException extends CustomException {
    public PromotionException(PromotionExceptionCode promotionExceptionCode) {
        super(promotionExceptionCode);
    }
    public static class PromotionNotFoundException extends PromotionException {
        public PromotionNotFoundException() {
            super(PromotionExceptionCode.PROMOTION_NOT_FOUND);
        }
    }

    public static class PromotionNotFinishedException extends PromotionException {
        public PromotionNotFinishedException() {
            super(PromotionExceptionCode.PROMOTION_NOT_FINISHED);
        }
    }

    public static class PromotionNotInProgressException extends PromotionException {
        public PromotionNotInProgressException() {
            super(PromotionExceptionCode.PROMOTION_NOT_IN_PROGRESS);
        }
    }

}