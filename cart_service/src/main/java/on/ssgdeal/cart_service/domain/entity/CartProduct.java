package on.ssgdeal.cart_service.domain.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("cart_product")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Builder(access = lombok.AccessLevel.PRIVATE)
@Getter
public class CartProduct {

    @Id
    private String hashKey;
    private Long quantity;

    public static CartProduct create(String hashKey, Long quantity) {
        return CartProduct.builder()
            .hashKey(hashKey)
            .quantity(quantity)
            .build();
    }

    @Override
    public String toString() {
        return "CartProduct{" +
            "hashKey='" + hashKey + '\'' +
            ", quantity=" + quantity +
            '}';
    }

    public void increaseQuantity(Long quantity) {
        this.quantity += quantity;
    }
}
