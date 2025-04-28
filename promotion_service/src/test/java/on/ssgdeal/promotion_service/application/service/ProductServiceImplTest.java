package on.ssgdeal.promotion_service.application.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import on.ssgdeal.common.application.dto.SliceDto;
import on.ssgdeal.promotion_service.application.service.dto.product.DecreaseStockRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.FindProductByPromotionIdRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.GetProductDetailsRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.IncreaseStockRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.UpdateProductRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.ValidateStockDecreasesRequestDto;
import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.repository.ProductRepository;
import on.ssgdeal.promotion_service.domain.repository.PromotionRepository;
import on.ssgdeal.promotion_service.exception.ProductException.ProductOptionNotFoundException;
import on.ssgdeal.promotion_service.exception.ProductException.ProductPromotionIsNotInProgressException;
import on.ssgdeal.promotion_service.infrastructure.persistence.cache.ProductCacheManager;
import on.ssgdeal.promotion_service.presentation.external.dto.product.FindByIdResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.FindByPromotionIdResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.UpdateProductResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.DecreaseStockResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.GetProductDetailsResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.IncreaseStockResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.ValidateStockDecreasesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("ProductServiceImpl 단위 테스트")
@Transactional
class ProductServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImplTest.class);

    @Autowired
    private ProductServiceImpl productServiceImpl;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ProductCacheManager productCacheManager;

    @MockitoBean(name = "loginUserAuditorAware")
    private AuditorAware<Long> auditorAware;


    @BeforeEach
    void setUpAuditor() {
        when(auditorAware.getCurrentAuditor())
            .thenReturn(Optional.empty());
    }

    @Nested
    @DisplayName("validateStockDecrease 메서드는")
    class ValidateStockDecreaseTests {

        @Nested
        @DisplayName("Context: 유효한 요청으로 재고 감소 검증 시")
        class WhenRequestIsValid {

            @Test
            @DisplayName("It: 회사별 그룹화된 응답 DTO를 반환한다.")
            void testValidateStockDecrease_valid() {
                Long productId = 3L;
                Long optionId = 5L;
                Long companyId = 2L;
                String companyName = "브런치타임";
                Long decreaseStockAmount = 1L;
                ValidateStockDecreasesRequestDto.ProductDetail detail =
                    ValidateStockDecreasesRequestDto.ProductDetail.builder()
                        .productId(productId)
                        .optionId(optionId)
                        .decreaseStockAmount(decreaseStockAmount)
                        .build();

                ValidateStockDecreasesRequestDto dto = ValidateStockDecreasesRequestDto.builder()
                    .getProductDetails(List.of(detail))
                    .build();

                ValidateStockDecreasesResponse response = productServiceImpl.validateStockDecrease(
                    dto);

                assertThat(response).isNotNull();
                List<ValidateStockDecreasesResponse.CompanyDetail> companyDetails = response.companyList();
                assertThat(companyDetails.size()).isEqualTo(1);

                ValidateStockDecreasesResponse.CompanyDetail companyDetail = companyDetails.get(0);
                assertThat(companyDetail.companyId()).isEqualTo(companyId);
                assertThat(companyDetail.companyName()).isEqualTo(companyName);

                List<ValidateStockDecreasesResponse.CompanyDetail.CompanyProduct> companyProducts =
                    companyDetail.companyProductList();
                assertThat(companyProducts.size()).isEqualTo(1);

                ValidateStockDecreasesResponse.CompanyDetail.CompanyProduct cp = companyProducts.get(
                    0);
                assertThat(cp.productId()).isEqualTo(productId);
                assertThat(cp.decreaseStockAmount()).isEqualTo(decreaseStockAmount);
            }
        }
    }

    @Nested
    @DisplayName("decreaseStock 메서드는")
    class DecreaseStockTests {

        @Nested
        @DisplayName("Context: 유효한 요청일 때")
        class ValidDecreaseStockTest {

            @Test
            @DisplayName("It: 재고를 정상적으로 감소시키고 DecreaseStockResponse를 반환한다.")
            void testDecreaseStock_Success() {
                // given
                Long productId = 3L;
                Long optionId = 5L;
                Long decreaseStockAmount = 1L;
                DecreaseStockRequestDto dto = new DecreaseStockRequestDto(productId, optionId,
                    decreaseStockAmount);

                // when
                DecreaseStockResponse response = productServiceImpl.decreaseStock(dto);

                // then
                assertThat(response).isNotNull();
                assertThat(response.productId()).isEqualTo(productId);
                assertThat(response.optionId()).isEqualTo(optionId);
                assertThat(response.decreaseStockAmount()).isEqualTo(decreaseStockAmount);
            }
        }

        @Nested
        @DisplayName("Context: 프로모션이 종료된 경우 (FINISHED)")
        class PromotionNotInProgressTest {

            @Test
            @DisplayName("It: ProductPromotionIsNotInProgressException 예외를 발생시킨다.")
            void testDecreaseStock_PromotionFinished() {
                // given
                Long productId = 1L;
                Long optionId = 1L;
                Long increaseStockAmount = 5L;
                DecreaseStockRequestDto dto = new DecreaseStockRequestDto(productId, optionId,
                    increaseStockAmount);

                // when, then
                assertThatThrownBy(() -> productServiceImpl.decreaseStock(dto))
                    .isInstanceOf(ProductPromotionIsNotInProgressException.class);
            }
        }
    }

    @Nested
    @DisplayName("increaseStock 메서드는")
    class IncreaseStockTests {

        @Nested
        @DisplayName("Context: 유효한 요청일 때")
        class ValidIncreaseStockTest {

            @Test
            @DisplayName("It: 재고를 정상적으로 증가시키고 IncreaseStockResponse를 반환한다.")
            void testIncreaseStock_Success() {
                // given
                Long productId = 1L;
                Long optionId = 1L;
                Long increaseStockAmount = 5L;
                IncreaseStockRequestDto dto = new IncreaseStockRequestDto(productId, optionId,
                    increaseStockAmount);

                // when
                IncreaseStockResponse response = productServiceImpl.increaseStock(dto);

                // then
                assertThat(response).isNotNull();
                assertThat(response.productId()).isEqualTo(productId);
                assertThat(response.optionId()).isEqualTo(optionId);
                assertThat(response.increaseStockAmount()).isEqualTo(increaseStockAmount);
            }
        }

        @Nested
        @DisplayName("Context: 올바르지 않은 상품 옵션인 경우 (Unmatched)")
        class PromotionNotInProgressTestForIncrease {

            @Test
            @DisplayName("It: ProductOptionNotFoundException 예외를 발생시킨다.")
            void testIncreaseStock_PromotionFinished() {
                // given
                Long productId = 1L;
                Long optionId = 5L;
                Long increaseStockAmount = 5L;
                IncreaseStockRequestDto dto = new IncreaseStockRequestDto(productId, optionId,
                    increaseStockAmount);

                // when, then
                assertThatThrownBy(() -> productServiceImpl.increaseStock(dto))
                    .isInstanceOf(ProductOptionNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindByIdTests {

        @Nested
        @DisplayName("Context: 유효한 요청일 때")
        class ValidFindByIdTest {

            @Test
            @Rollback(false)
            @DisplayName("It: 캐시에서 값을 확인 후, RDB에서 값을 조회하며, 캐시 업로드를 수행한다.")
            void testFindById_Success() {
                // given
                Long productId = 3L;
                Product product = productRepository.findById(productId).orElse(null);

                // when
                for (int i = 0; i < 10; i++) {
                    long start = System.currentTimeMillis();
                    FindByIdResponse response = productServiceImpl.findById(productId);
                    long end = System.currentTimeMillis();
                    log.info("findById({}) execution time: {} ms", productId, (end - start));

                }
                FindByIdResponse response = productServiceImpl.findById(productId);

                // then
                assertThat(response.productId()).isEqualTo(productId);
                assertThat(response.companyName()).isEqualTo(
                    product.getCompany().getName().getValue());
                assertThat(response.productContent()).isEqualTo(product.getContent());
                assertThat(response.productPreviewImgUrl()).isEqualTo(
                    product.getPreviewUrl().getValue());

            }
        }
    }

    @Nested
    @DisplayName("findByPromotionId 메서드는")
    class FindByPromotionIdTests {

        @Nested
        @DisplayName("Context: 유효한 요청일 때")
        class ValidFindByPromotionIdTests {

            @Test
            @Rollback(false)
            @DisplayName("It: 캐시에서 값을 확인 후, RDB에서 값을 조회하며, 캐시 업로드를 수행한다.")
            void testFindByPromotionIdTests_Success() {
                // given
                Long promotionId = 2L;
                Pageable pageable = Pageable.ofSize(10);
                FindProductByPromotionIdRequestDto dto = new FindProductByPromotionIdRequestDto(
                    promotionId, pageable);
                Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
                Slice<Product> products = productRepository.findByCompanyId(promotion.getCompany()
                    .getId(), pageable);

                // when
                for (int i = 0; i < 10; i++) {
                    long start = System.currentTimeMillis();
                    SliceDto<FindByPromotionIdResponse> response = productServiceImpl.findByPromotionId(
                        dto);
                    long end = System.currentTimeMillis();
                    log.info("findById({}) execution time: {} ms", promotionId, (end - start));

                }
                SliceDto<FindByPromotionIdResponse> response = productServiceImpl.findByPromotionId(
                    dto);

                // then
                Product product = products.getContent().get(0);
                assertThat(response.content().get(0).companyName()).isEqualTo(
                    product.getCompany().getName().getValue());
                assertThat(response.content().get(0).productId()).isEqualTo(product.getId());
                assertThat(response.content().get(0).productPreviewImgUrl()).isEqualTo(
                    product.getPreviewUrl().getValue());

            }
        }
    }

    @Nested
    @DisplayName("getProductDetails 메서드는")
    class GetProductDetailsTests {

        @Nested
        @DisplayName("Context: 유효한 요청일 때")
        class ValidGetProductDetailsTests {

            @Test
            @Rollback(false)
            @DisplayName("It: 캐시에서 값을 확인 후, RDB에서 값을 조회하며, 캐시 업로드를 수행한다.")
            void testGetProductDetailsTests_Success() {
                // given
                Long productId = 2L;
                Long optionId = 4L;

                GetProductDetailsRequestDto.ProductDetail detail = new GetProductDetailsRequestDto
                    .ProductDetail(productId, optionId);

                GetProductDetailsRequestDto dto = new GetProductDetailsRequestDto(List.of(detail));

                Product product = productRepository.findById(productId).orElse(null);

                // when
                for (int i = 0; i < 10; i++) {
                    long start = System.currentTimeMillis();
                    GetProductDetailsResponse response = productServiceImpl.getProductDetails(dto);
                    long end = System.currentTimeMillis();
                    log.info("findById({}) execution time: {} ms", productId, (end - start));

                }
                GetProductDetailsResponse response = productServiceImpl.getProductDetails(dto);

                // then
                assertThat(response.productDetails().get(0).companyName()).isEqualTo(
                    product.getCompany().getName().getValue());
                assertThat(response.productDetails().get(0).productId()).isEqualTo(product.getId());
                assertThat(response.productDetails().get(0).productPreviewImgUrl()).isEqualTo(
                    product.getPreviewUrl().getValue());

            }
        }
    }

    @Nested
    @DisplayName("updateProduct 메서드는")
    class UpdateProductTests {

        @Nested
        @DisplayName("Context: 유효한 요청일 때")
        class WhenRequestIsValid {

            @Test
            @Rollback(false)
            @DisplayName("It: 제품을 업데이트하고 캐시를 갱신한 후 올바른 응답을 반환한다.")
            void testUpdateProduct_Success() {
                // given
                Long productId = 3L;
                String productName = "테스트 상품명";
                Long originalPrice = 30_000L;
                UpdateProductRequestDto dto = new UpdateProductRequestDto(
                    productId,
                    productName,
                    originalPrice,
                    null,
                    null,
                    null,
                    null
                );

                // 기존 상품 엔티티 준비
                Product existing = productRepository.findById(productId).orElse(null);

                // when
                UpdateProductResponse response = productServiceImpl.updateProduct(dto);

                // then: 반환 DTO 검증
                assert existing != null;
                assertThat(response.content()).isEqualTo(existing.getContent());
                assertThat(response.contentImgUrl()).isEqualTo(
                    existing.getContentImgUrl().getValue());
                assertThat(response.productId()).isEqualTo(productId);
                assertThat(response.productName()).isEqualTo(productName);
                assertThat(response.originalPrice()).isEqualTo(originalPrice);

            }
        }
    }

}