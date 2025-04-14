package on.ssgdeal.cart_service.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import on.ssgdeal.cart_service.infrastructure.persistence.repository.dto.UpdateCartProductDto;
import on.ssgdeal.cart_service.domain.entity.CartProduct;
import on.ssgdeal.cart_service.infrastructure.persistence.repository.dto.AddCartProductDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@DisplayName("장바구니 레포지토리 테스트")
class CartRepositoryImplTest {

    @Autowired
    private CartRepositoryImpl cartRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final String key = "cart:0";
    private final String hashKey = "product:0";
    private final Long quantity = 5L;

    @AfterEach
    void tearDown() {
        redisTemplate.delete(key);
    }

    @Nested
    @DisplayName("장바구니 상품을 찾는 findCartProduct 메서드는")
    class Describe_FindCartProduct {

        @Nested
        @DisplayName("장바구니에 상품이 존재할 때")
        class Context_WithCartProduct {

            @BeforeEach
            void setUp() {
                redisTemplate.opsForHash().put(key, hashKey, quantity);
            }

            @Test
            @DisplayName("장바구니에서 상품을 찾는다")
            void it_findCartProduct() {
                // when
                Optional<CartProduct> cartProduct = cartRepository.findCartProduct(key, hashKey);

                // then
                assertThat(cartProduct).isPresent();
                assertThat(cartProduct.get().getHashKey()).isEqualTo(hashKey);
                assertThat(cartProduct.get().getQuantity()).isEqualTo(quantity);
            }
        }

        @Nested
        @DisplayName("장바구니에 상품이 존재하지 않을 때")
        class Context_WithoutCartProduct {

            @Test
            @DisplayName("장바구니에서 상품을 찾지 못한다")
            void it_findCartProduct_NotFound() {
                // when
                Optional<CartProduct> cartProduct = cartRepository.findCartProduct(key, hashKey);

                // then
                assertThat(cartProduct).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("상품을 추가하는 addCartProduct 메서드는")
    class Describe_AddCartProduct {

        @Nested
        @DisplayName("장바구니에 상품이 존재하지 않을 때")
        class Context_WithoutCartProduct {

            @Test
            @DisplayName("장바구니에 상품을 추가한다")
            void it_addCartProduct() {
                // when
                var addCartProductDto = AddCartProductDto.from(key, hashKey, quantity);
                cartRepository.addCartProduct(addCartProductDto);

                // then
                Long loadedQuantity = (Long) redisTemplate.opsForHash().get(key, hashKey);
                assertThat(loadedQuantity).isNotNull();
                assertThat(loadedQuantity).isEqualTo(quantity);
            }
        }

        @Nested
        @DisplayName("장바구니에 상품이 존재할 때")
        class Context_WithCartProduct {

            @BeforeEach
            void setUp() {
                redisTemplate.opsForHash().put(key, hashKey, quantity);
            }

            @Test
            @DisplayName("장바구니에 상품을 추가한다")
            void it_addCartProduct() {
                // when
                var addCartProductDto = AddCartProductDto.from(key, hashKey, quantity);
                cartRepository.addCartProduct(addCartProductDto);

                // then
                Long loadedQuantity = (Long) redisTemplate.opsForHash().get(key, hashKey);
                assertThat(loadedQuantity).isNotNull();
                assertThat(loadedQuantity).isEqualTo(quantity);
            }
        }
    }

    @Nested
    @DisplayName("장바구니 상품을 삭제하는 deleteCartProducts 메서드는")
    class Describe_deleteCartProducts {

        @Nested
        @DisplayName("장바구니에 상품이 하나 존재할 때")
        class Context_WithCartProduct {

            @BeforeEach
            void setUp() {
                redisTemplate.opsForHash().put(key, hashKey, quantity);
            }

            @Test
            @DisplayName("장바구니에서 해당 상품을 삭제한다.")
            void it_deletesCartProduct() {
                // when
                cartRepository.deleteCartProducts(key, List.of(hashKey));

                Object get = redisTemplate.opsForHash().get(key, hashKey);
                assertThat(get).isNull();
            }
        }

        @Nested
        @DisplayName("장바구니에 상품이 여러 개 존재할 때")
        class Context_WithMultipleCartProducts {

            @BeforeEach
            void setUp() {
                redisTemplate.opsForHash().put(key, hashKey, quantity);
                redisTemplate.opsForHash().put(key, "product:1", 3L);
                redisTemplate.opsForHash().put(key, "product:2", 2L);
            }

            @Test
            @DisplayName("장바구니에서 해당 상품들을 삭제한다.")
            void it_deletesCartProducts() {
                // when
                cartRepository.deleteCartProducts(key, List.of(hashKey, "product:1", "product:2"));

                Object get0 = redisTemplate.opsForHash().get(key, hashKey);
                Object get1 = redisTemplate.opsForHash().get(key, "product:1");
                Object get2 = redisTemplate.opsForHash().get(key, "product:2");

                assertThat(get0).isNull();
                assertThat(get1).isNull();
                assertThat(get2).isNull();
            }
        }
    }
}
