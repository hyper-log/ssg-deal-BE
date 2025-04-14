package on.ssgdeal.order_service.domain.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import on.ssgdeal.order_service.domain.vo.TotalOrderNumber;
import on.ssgdeal.order_service.domain.vo.TotalPrice;
import on.ssgdeal.order_service.exception.OrderException.OrderFormatTotalOrderNumberException;
import on.ssgdeal.order_service.exception.OrderException.OrderNullPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TotalOrderTest {

    @Test
    @DisplayName("가격 빈값 검증")
    void insertValidBlankPrice() throws Exception {
        //given
        Long price = null;

        //when & then
        assertThatThrownBy(() -> new TotalPrice(price))
            .isInstanceOf(OrderNullPriceException.class);
    }

    @Test
    @DisplayName("주문번호 포맷팅 검증")
    void insertValidOrderNumber() throws Exception {
        //given
        String orderNumber = "굿";

        //when & then
        assertThatThrownBy(() -> new TotalOrderNumber(orderNumber))
            .isInstanceOf(OrderFormatTotalOrderNumberException.class);
    }
}