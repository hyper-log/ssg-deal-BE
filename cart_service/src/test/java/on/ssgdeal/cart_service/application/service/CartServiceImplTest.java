package on.ssgdeal.cart_service.application.service;

import static on.ssgdeal.cart_service.exception.CartException.NotEnoughStockException;
import static org.assertj.core.api.Assertions.assertThat;

import on.ssgdeal.cart_service.application.service.dto.AddCartProductRequestDto;
import on.ssgdeal.cart_service.domain.entity.CartProduct;
import on.ssgdeal.cart_service.domain.repository.CartRepository;
import on.ssgdeal.cart_service.infrastructure.client.product.dto.IsProductStockAvailableRequestDto;
import on.ssgdeal.cart_service.infrastructure.persistence.generator.RedisKeyGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@DisplayName("장바구니 서비스 테스트")
class CartServiceImplTest {

    private final CartService cartService;

    public CartServiceImplTest(
        @Autowired CartRepository cartRepository
    ) {
        this.cartService = new CartServiceImpl(
            cartRepository,
            new ProductService() {
                @Override
                public void isProductStockAvailable(IsProductStockAvailableRequestDto requestDto) {
                    if (requestDto.quantity() <= 0) {
                        throw new NotEnoughStockException();
                    }
                }
            }
        );
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final Long userId = 0L;
    private final Long productId = 0L;
    private final Long optionId = 0L;
    private final Long quantity = 5L;

    private final String key = RedisKeyGenerator.generateKey(userId);
    private final String hashKey = RedisKeyGenerator.generateHashKey(productId, optionId);

    @AfterEach
    void tearDown() {
        redisTemplate.delete(key);
    }

    @Nested
    @DisplayName("요청받은 상품을 추가하는 addCartProduct 메서드")
    class Describe_AddCartProduct {

        private AddCartProductRequestDto requestDto;

        @BeforeEach
        void setUp() {
            requestDto = new AddCartProductRequestDto(
                userId, productId, optionId, quantity);
        }

        @Nested
        @DisplayName("장바구니에 동일한 상품이 존재하지 않을 때")
        class Context_WithoutCartProduct {

            @Test
            @DisplayName("새로운 상품을 추가한다")
            void it_addCartProduct() {
                // when
                cartService.addCartProduct(requestDto);

                // then
                Object get = redisTemplate.opsForHash().get(key, hashKey);
                assertThat(get).isNotNull();
                assertThat(get).isEqualTo(quantity);
            }
        }

        @Nested
        @DisplayName("장바구니에 동일한 상품이 이미 존재할 때")
        class Context_WithCartProduct {

            private final long existingProductQuantity = 1L;

            private CartProduct existingProduct;

            @BeforeEach
            void setUp() {
                existingProduct = CartProduct.create(hashKey, existingProductQuantity);
                redisTemplate.opsForHash().put(
                    key, existingProduct.getHashKey(), existingProduct.getQuantity());
            }

            @Test
            @DisplayName("기존 상품의 수량을 증가시킨다")
            void it_addCartProduct() {
                // when
                cartService.addCartProduct(requestDto);

                // then
                Object get = redisTemplate.opsForHash().get(key, existingProduct.getHashKey());
                assertThat(get).isNotNull();
                assertThat(get).isEqualTo(existingProductQuantity + quantity);
            }
        }
    }
}