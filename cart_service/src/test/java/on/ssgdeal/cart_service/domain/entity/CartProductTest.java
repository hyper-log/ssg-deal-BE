package on.ssgdeal.cart_service.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CartProductTest {

    @Nested
    @DisplayName("장바구니 상품을 생성하는 create 메서드는")
    class Describe_Create {

        @Test
        @DisplayName("장바구니 상품을 생성한다")
        void it_createCartProduct() {
            // given
            String hashKey = "product:0";
            Long quantity = 5L;

            // when
            CartProduct cartProduct = CartProduct.create(hashKey, quantity);

            // then
            assertThat(cartProduct.getHashKey()).isEqualTo(hashKey);
            assertThat(cartProduct.getQuantity()).isEqualTo(quantity);
        }
    }

    @Nested
    @DisplayName("장바구니 상품의 수량을 증가시키는 increaseQuantity 메서드는")
    class Describe_IncreaseQuantity {

        @Test
        @DisplayName("장바구니 상품의 수량을 증가시킨다")
        void it_increaseCartProductQuantity() {
            // given
            String hashKey = "product:0";
            Long initialQuantity = 5L;
            Long increaseQuantity = 3L;
            CartProduct cartProduct = CartProduct.create(hashKey, initialQuantity);

            // when
            cartProduct.increaseQuantity(increaseQuantity);

            // then
            assertThat(cartProduct.getHashKey()).isEqualTo(hashKey);
            assertThat(cartProduct.getQuantity()).isEqualTo(
                initialQuantity + increaseQuantity);
        }
    }
}