package on.ssgdeal.cart_service.domain.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.cart_service.domain.vo.ProductQuantity;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("cart_product")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Builder(access = lombok.AccessLevel.PRIVATE)
public class CartProduct {

    @Id
    @Getter
    private String hashKey;
    private ProductQuantity quantity;

    public static CartProduct create(String hashKey, Long quantity) {
        return CartProduct.builder()
            .hashKey(hashKey)
            .quantity(new ProductQuantity(quantity))
            .build();
    }

    public void increaseQuantity(Long quantity) {
        this.quantity.increase(quantity);
    }

    public Long getQuantity() {
        return this.quantity.getValue();
    }
}
