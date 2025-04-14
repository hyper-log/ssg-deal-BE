package on.ssgdeal.order_service.domain.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import on.ssgdeal.order_service.domain.vo.DeliveryRequest;
import on.ssgdeal.order_service.exception.OrderException.OrderMaxDeliveryRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrdererTest {

    @Test
    @DisplayName("배송 요청 사항 초과 검증")
    void validDeliveryRequest() throws Exception {
        //given
        String deliveryRequest = "20자이상초과테스트20자이상초과테스트20자이상초과테스트";

        //when & then
        assertThatThrownBy(() -> new DeliveryRequest(deliveryRequest)).isInstanceOf(
            OrderMaxDeliveryRequestException.class);
    }
}