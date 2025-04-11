package on.ssgdeal.order_service.exception;

import on.ssgdeal.common.global.exception.CustomException;

public class OrderException extends CustomException {

    public OrderException(OrderExceptionCode orderExceptionCode) {
        super(orderExceptionCode);
    }

    public static class OrderNullPriceException extends OrderException {

        public OrderNullPriceException() {
            super(OrderExceptionCode.ORDER_NULL_PRICE);
        }
    }

    public static class OrderMinPriceException extends OrderException {

        public OrderMinPriceException() {
            super(OrderExceptionCode.ORDER_MIN_PRICE);
        }
    }

    public static class OrderNullTotalOrderNumber extends OrderException {

        public OrderNullTotalOrderNumber() {
            super(OrderExceptionCode.ORDER_NULL_TOTAL_ORDER_NUMBER);
        }
    }

    public static class OrderFormatTotalOrderNumberException extends OrderException {

        public OrderFormatTotalOrderNumberException() {
            super(OrderExceptionCode.ORDER_FORMAT_TOTAL_ORDER_NUMBER);
        }
    }

    public static class OrderNullStatusException extends OrderException {

        public OrderNullStatusException() {
            super(OrderExceptionCode.ORDER_NULL_STATUS);
        }
    }

    public static class OrderMaxDeliveryRequestException extends OrderException {

        public OrderMaxDeliveryRequestException() {
            super(OrderExceptionCode.ORDER_MAX_DELIVERY_REQUEST);
        }
    }

    public static class OrderPromotionFinished extends OrderException {

        public OrderPromotionFinished() {
            super(OrderExceptionCode.ORDER_PROMOTION_FINISHED);
        }
    }

    public static class OrderPromotionStockOver extends OrderException {

        public OrderPromotionStockOver() {
            super(OrderExceptionCode.ORDER_PROMOTION_STOCK_OVER);
        }

    }

    public static class OrderValidDestination extends OrderException {

        public OrderValidDestination() {
            super(OrderExceptionCode.ORDER_VALID_DESTINATION);
        }
    }

    public static class OrderNotFoundTotalOrderException extends OrderException {

        public OrderNotFoundTotalOrderException() {
            super(OrderExceptionCode.ORDER_NOT_FOUND_TOTAL_ORDER);
        }
    }

    public static class OrderMinQuantityException extends OrderException {

        public OrderMinQuantityException() {
            super(OrderExceptionCode.ORDER_MIN_QUANTITY);
        }
    }
}
