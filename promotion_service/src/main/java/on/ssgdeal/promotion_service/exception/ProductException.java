package on.ssgdeal.promotion_service.exception;

import on.ssgdeal.common.global.exception.CustomException;

public class ProductException extends CustomException {

    public ProductException(ProductExceptionCode productExceptionCode) {
        super(productExceptionCode);
    }

    public static class ProductNotFoundException extends ProductException {

        public ProductNotFoundException() {
            super(ProductExceptionCode.PRODUCT_NOT_FOUND);
        }
    }

    public static class ProductOptionNotFoundException extends ProductException {

        public ProductOptionNotFoundException() {
            super(ProductExceptionCode.PRODUCT_OPTION_NOT_FOUND);
        }
    }

    public static class ProductDoNotExistException extends ProductException {

        public ProductDoNotExistException() {
            super(ProductExceptionCode.PRODUCT_DO_NOT_EXIST);
        }
    }

    public static class ProductCompanyMismatchException extends ProductException {

        public ProductCompanyMismatchException() {
            super(ProductExceptionCode.PRODUCT_COMPANY_MISMATCH);
        }
    }

    public static class ProductPromotionFinishedException extends ProductException {

        public ProductPromotionFinishedException() {
            super(ProductExceptionCode.PRODUCT_PROMOTION_FINISHED);
        }
    }

    public static class ProductPromotionIsNotInProgressException extends ProductException {

        public ProductPromotionIsNotInProgressException() {
            super(ProductExceptionCode.PRODUCT_PROMOTION_IS_NOT_IN_PROGRESS);
        }
    }

}