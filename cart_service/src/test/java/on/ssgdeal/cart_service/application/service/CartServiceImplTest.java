package on.ssgdeal.cart_service.application.service;

import static on.ssgdeal.cart_service.exception.CartException.NotEnoughStockException;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import on.ssgdeal.cart_service.application.generator.RedisKeyGenerator;
import on.ssgdeal.cart_service.application.service.dto.AddCartProductRequestDto;
import on.ssgdeal.cart_service.application.service.dto.GetProductsByIdsResponseDto;
import on.ssgdeal.cart_service.domain.entity.CartProduct;
import on.ssgdeal.cart_service.domain.repository.CartRepository;
import on.ssgdeal.cart_service.infrastructure.client.product.dto.GetProductOptionsResponseDto;
import on.ssgdeal.cart_service.infrastructure.client.product.dto.IsProductStockAvailableRequestDto;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductDetailsResponse;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductDetailsResponse.ProductDetail;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductDetailsResponse.ProductDetail.PromotionStatus;
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

    public CartServiceImplTest(
        @Autowired CartRepository cartRepository
    ) {
        this.cartService = new CartServiceImpl(
            cartRepository,
            new ProductService() {
                @Override
                public GetProductDetailsResponse getProductsByHashKeys(
                    List<CartProduct> cartProducts
                ) {
                    List<ProductDetail> productDetails = List.of(
                        new ProductDetail(
                            PromotionStatus.IN_PROGRESS,
                            1L,
                            "companyName_1",
                            1L,
                            "productName_1",
                            "imgUrl_1",
                            1000L,
                            100L,
                            1L,
                            "optionName_1",
                            0L
                        ),
                        new ProductDetail(
                            PromotionStatus.IN_PROGRESS,
                            1L,
                            "companyName_1",
                            2L,
                            "productName_2",
                            "imgUrl_2",
                            2000L,
                            200L,
                            1L,
                            "optionName_1",
                            0L
                        ),
                        new ProductDetail(
                            PromotionStatus.IN_PROGRESS,
                            2L,
                            "companyName_2",
                            3L,
                            "productName_3",
                            "imgUrl_3",
                            3000L,
                            300L,
                            1L,
                            "optionName_1",
                            500L
                        )
                    );
                    return new GetProductDetailsResponse(productDetails);
                }
                @Override
                public List<GetProductOptionsResponseDto> getProductOptions(
                    List<CartProduct> cartProducts
                ) {
                    return List.of(
                        new GetProductOptionsResponseDto(
                            1L,
                            List.of(
                                new GetProductOptionsResponseDto.Option(
                                    1L,
                                    "optionName_1",
                                    0L,
                                    10L
                                ),
                                new GetProductOptionsResponseDto.Option(
                                    2L,
                                    "optionName_2",
                                    0L,
                                    20L
                                )
                            )
                        ),
                        new GetProductOptionsResponseDto(
                            2L,
                            List.of(
                                new GetProductOptionsResponseDto.Option(
                                    1L,
                                    "optionName_1",
                                    0L,
                                    30L
                                )
                            )
                        ),
                        new GetProductOptionsResponseDto(
                            3L,
                            List.of(
                                new GetProductOptionsResponseDto.Option(
                                    1L,
                                    "optionName_1",
                                    500L,
                                    40L
                                )
                            )
                        )
                    );
                }
                @Override
                public void isProductStockAvailable(IsProductStockAvailableRequestDto requestDto) {
                    if (requestDto.quantity() <= 0) {
                        throw new NotEnoughStockException();
                    }
                }
            }
        );
    }

    @Nested
    @DisplayName("장바구니 상품을 조회하는 getCarts 메서드는")
    class Describe_getCarts {

        @Nested
        @DisplayName("장바구니에 상품이 존재할 때")
        class Context_with_cart_product {

            @BeforeEach
            void setUp() {
                String hashKey1 = RedisKeyGenerator.generateHashKey(1L, 1L);
                String hashKey2 = RedisKeyGenerator.generateHashKey(2L, 1L);
                String hashKey3 = RedisKeyGenerator.generateHashKey(3L, 1L);
                redisTemplate.opsForHash().putAll(
                    key,
                    Map.of(
                        hashKey1, quantity,
                        hashKey2, quantity,
                        hashKey3, quantity
                    )
                );
            }

            @Test
            @DisplayName("장바구니 상품을 조회한다")
            void it_gets_cart_product() {
                // when
                GetProductsByIdsResponseDto response = cartService.getCarts(userId);

                // then
                assertThat(response).isNotNull();
                assertThat(response.productCount()).isEqualTo(3);
                assertThat(response.subCarts()).hasSize(2);
            }
        }
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
                Object get = redisTemplate.opsForHash()
                    .get(key, existingProduct.getHashKey());
                assertThat(get).isNotNull();
                assertThat(get).isEqualTo(existingProductQuantity + quantity);
            }
        }
    }
}