package on.ssgdeal.order_service.domain.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import on.ssgdeal.order_service.domain.vo.Price;
import on.ssgdeal.order_service.exception.OrderException.OrderNullPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    @DisplayName("가격 빈값 검증")
    void validPrice() throws Exception {
        //given & when & then
        assertThatThrownBy(() -> new Price(null)).isInstanceOf(
            OrderNullPriceException.class);
    }

}
