package on.ssgdeal.order_service.domain.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import on.ssgdeal.order_service.domain.vo.TotalOrderNumber;
import on.ssgdeal.order_service.domain.vo.TotalPrice;
import on.ssgdeal.order_service.exception.OrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TotalOrderTest {

    @Test
    @DisplayName("가격 빈값 검증")
    void insertValidBlankPrice() throws Exception {
        //given
        BigDecimal price = null;

        //when & then
        assertThatThrownBy(() -> new TotalPrice(price))
            .isInstanceOf(OrderException.class);
    }

    @Test
    @DisplayName("최저 가격 검증")
    void insertValidMinPrice() throws Exception {
        //given
        BigDecimal price = new BigDecimal("0.00");

        //when & then
        assertThatThrownBy(() -> new TotalPrice(price))
            .isInstanceOf(OrderException.class);
    }

    @Test
    @DisplayName("주문번호 포맷팅 검증")
    void insertValidOrderNumber() throws Exception {
        //given
        String orderNumber = "굿";

        //when & then
        assertThatThrownBy(() -> new TotalOrderNumber(orderNumber))
            .isInstanceOf(OrderException.class);
    }
}