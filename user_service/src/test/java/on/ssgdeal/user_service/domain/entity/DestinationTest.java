package on.ssgdeal.user_service.domain.entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import on.ssgdeal.user_service.domain.vo.Address;
import on.ssgdeal.user_service.exception.destination.DestinationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DestinationTest {

    @Test
    @DisplayName("Address Null 실패 검증")
    void validAddress_fail() throws Exception {
        String address = null;
        assertThatThrownBy(() -> new Address(address)).isInstanceOf(
            DestinationException.class);
    }

    @Test
    @DisplayName("Address 성공 검증")
    void validAddress_success() throws Exception {
        String address = "충남 천안시 서북구";
        assertThatNoException().isThrownBy(() -> new Address(address));
    }

}
