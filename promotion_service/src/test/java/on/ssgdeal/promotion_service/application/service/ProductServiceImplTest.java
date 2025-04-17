package on.ssgdeal.promotion_service.application.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import on.ssgdeal.promotion_service.application.service.dto.product.DecreaseStockRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.IncreaseStockRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.ValidateStockDecreasesRequestDto;
import on.ssgdeal.promotion_service.domain.entity.Company;
import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.ProductOption;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.mapper.ProductMapper;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import on.ssgdeal.promotion_service.domain.repository.ProductRepository;
import on.ssgdeal.promotion_service.domain.repository.PromotionRepository;
import on.ssgdeal.promotion_service.domain.vo.CompanyName;
import on.ssgdeal.promotion_service.domain.vo.OptionName;
import on.ssgdeal.promotion_service.domain.vo.ProductName;
import on.ssgdeal.promotion_service.domain.vo.ProductOptionExtraPrice;
import on.ssgdeal.promotion_service.domain.vo.ProductOriginalPrice;
import on.ssgdeal.promotion_service.domain.vo.ProductPreviewUrl;
import on.ssgdeal.promotion_service.domain.vo.ProductPromotionPrice;
import on.ssgdeal.promotion_service.domain.vo.ProductStock;
import on.ssgdeal.promotion_service.exception.PromotionException;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.DecreaseStockResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.IncreaseStockResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.ValidateStockDecreasesResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductServiceImpl 단위 테스트")
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private ProductMapper productMapper; // 이번 테스트에서는 사용하지 않음

    @Nested
    @DisplayName("validateStockDecrease 메서드는")
    class ValidateStockDecreaseTests {

        @Nested
        @DisplayName("Context: 유효한 요청으로 재고 감소 검증 시")
        class WhenRequestIsValid {

            @Test
            @DisplayName("It: 회사별 그룹화된 응답 DTO를 반환한다.")
            void testValidateStockDecrease_valid() {
                ValidateStockDecreasesRequestDto.ProductDetail requestDetail =
                    new ValidateStockDecreasesRequestDto.ProductDetail(1L, 100L, 5L);
                ValidateStockDecreasesRequestDto requestDto =
                    new ValidateStockDecreasesRequestDto(List.of(requestDetail));

                // Company
                Company company = mock(Company.class);
                when(company.getId()).thenReturn(1L);
                CompanyName companyNameVO = mock(CompanyName.class);
                when(companyNameVO.getValue()).thenReturn("Test Company");
                when(company.getName()).thenReturn(companyNameVO);

                // Promotion (검증 로직에서 IN_PROGRESS이면 예외 발생하므로, FINISHED 상태여야 함)
                Promotion promotion = mock(Promotion.class);
                when(promotion.getStatus()).thenReturn(PromotionStatus.FINISHED);
                when(company.getPromotion()).thenReturn(promotion);

                // Product
                Product product = mock(Product.class);
                when(product.getId()).thenReturn(10L);
                when(product.getCompany()).thenReturn(company);
                ProductName productNameVO = mock(ProductName.class);
                when(productNameVO.getValue()).thenReturn("Test Product");
                when(product.getName()).thenReturn(productNameVO);
                ProductPreviewUrl previewUrlVO = mock(ProductPreviewUrl.class);
                when(previewUrlVO.getValue()).thenReturn("http://example.com/preview");
                when(product.getPreviewUrl()).thenReturn(previewUrlVO);
                ProductOriginalPrice originalPriceVO = mock(ProductOriginalPrice.class);
                when(originalPriceVO.getValue()).thenReturn(20000L);
                when(product.getOriginalPrice()).thenReturn(originalPriceVO);
                ProductPromotionPrice promotionPriceVO = mock(ProductPromotionPrice.class);
                when(promotionPriceVO.getValue()).thenReturn(15000L);
                when(product.getPromotionPrice()).thenReturn(promotionPriceVO);

                // ProductOption
                ProductOption productOption = mock(ProductOption.class);
                when(productOption.getId()).thenReturn(100L);
                OptionName optionNameVO = mock(OptionName.class);
                when(optionNameVO.getValue()).thenReturn("Red / L");
                when(productOption.getOptionName()).thenReturn(optionNameVO);
                ProductOptionExtraPrice extraPriceVO = mock(ProductOptionExtraPrice.class);
                when(extraPriceVO.getValue()).thenReturn(100L);
                when(productOption.getExtraPrice()).thenReturn(extraPriceVO);

                ProductStock stockVO = mock(ProductStock.class);
                when(stockVO.getValue()).thenReturn(10L);
                when(productOption.getProductStock()).thenReturn(stockVO);

                when(product.getOptions()).thenReturn(List.of(productOption));

                when(productRepository.findAllWithDetailsByProductIdsAndOptionIds(anyList(),
                    anyList()))
                    .thenReturn(List.of(product));

                ValidateStockDecreasesResponse response = productServiceImpl.validateStockDecrease(
                    requestDto);

                assertThat(response).isNotNull();
                List<ValidateStockDecreasesResponse.CompanyDetail> companyDetails = response.companyList();
                assertThat(companyDetails.size()).isEqualTo(1);

                ValidateStockDecreasesResponse.CompanyDetail companyDetail = companyDetails.get(0);
                assertThat(companyDetail.companyId()).isEqualTo(1L);
                assertThat(companyDetail.companyName()).isEqualTo("Test Company");

                List<ValidateStockDecreasesResponse.CompanyDetail.CompanyProduct> companyProducts =
                    companyDetail.companyProductList();
                assertThat(companyProducts.size()).isEqualTo(1);

                ValidateStockDecreasesResponse.CompanyDetail.CompanyProduct cp = companyProducts.get(
                    0);
                assertThat(cp.productId()).isEqualTo(10L);
                assertThat(cp.productName()).isEqualTo("Test Product");
                assertThat(cp.productPreview()).isEqualTo("http://example.com/preview");
                assertThat(cp.originalPrice()).isEqualTo(20000L);
                assertThat(cp.promotionPrice()).isEqualTo(15000L);
                assertThat(cp.optionId()).isEqualTo(100L);
                assertThat(cp.optionName()).isEqualTo("Red / L");
                assertThat(cp.extraPrice()).isEqualTo(100L);
                assertThat(cp.decreaseStockAmount()).isEqualTo(5L);
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
                DecreaseStockRequestDto dto = new DecreaseStockRequestDto(10L, 100L, 5L);
                Company company = org.mockito.Mockito.mock(Company.class);

                Promotion promotion = org.mockito.Mockito.mock(Promotion.class);
                when(promotion.getStatus()).thenReturn(PromotionStatus.IN_PROGRESS);
                when(company.getPromotion()).thenReturn(promotion);

                Product product = org.mockito.Mockito.mock(Product.class);
                when(product.getCompany()).thenReturn(company);

                ProductOption productOption = org.mockito.Mockito.mock(ProductOption.class);
                when(productOption.getId()).thenReturn(100L);
                ProductStock stockVO = org.mockito.Mockito.mock(ProductStock.class);
                when(productOption.getProductStock()).thenReturn(stockVO);
                when(product.getOptions()).thenReturn(List.of(productOption));
                when(productRepository.findById(dto.productId())).thenReturn(Optional.of(product));
                when(productRepository.save(product)).thenReturn(product);

                DecreaseStockResponse expectedResponse = DecreaseStockResponse.builder()
                    .productId(10L)
                    .optionId(100L)
                    .decreaseStockAmount(5L)
                    .build();
                when(productMapper.toDecreaseStockResponse(product, productOption,
                    dto.decreaseStockAmount()))
                    .thenReturn(expectedResponse);

                // when
                DecreaseStockResponse response = productServiceImpl.decreaseStock(dto);

                // then
                assertThat(response).isNotNull();
                assertThat(response.productId()).isEqualTo(10L);
                assertThat(response.optionId()).isEqualTo(100L);
                assertThat(response.decreaseStockAmount()).isEqualTo(5);
            }
        }

        @Nested
        @DisplayName("Context: 프로모션이 종료된 경우 (FINISHED)")
        class PromotionNotInProgressTest {

            @Test
            @DisplayName("It: PromotionException.PromotionNotInProgressException 예외를 발생시킨다.")
            void testDecreaseStock_PromotionFinished() {
                // given
                DecreaseStockRequestDto dto = new DecreaseStockRequestDto(10L, 100L, 5L);

                Company company = org.mockito.Mockito.mock(Company.class);

                Promotion promotion = org.mockito.Mockito.mock(Promotion.class);
                when(promotion.getStatus()).thenReturn(PromotionStatus.FINISHED);
                when(company.getPromotion()).thenReturn(promotion);

                Product product = org.mockito.Mockito.mock(Product.class);
                when(product.getCompany()).thenReturn(company);

                when(productRepository.findById(dto.productId())).thenReturn(Optional.of(product));

                // when, then
                assertThatThrownBy(() -> productServiceImpl.decreaseStock(dto))
                    .isInstanceOf(PromotionException.PromotionNotInProgressException.class);
            }
        }
    }

    // ---------------------------
    // increaseStock 메서드 테스트
    // ---------------------------
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
                IncreaseStockRequestDto dto = new IncreaseStockRequestDto(10L, 100L, 3L);

                Company company = org.mockito.Mockito.mock(Company.class);

                Promotion promotion = org.mockito.Mockito.mock(Promotion.class);
                when(promotion.getStatus()).thenReturn(PromotionStatus.IN_PROGRESS);
                when(company.getPromotion()).thenReturn(promotion);

                Product product = org.mockito.Mockito.mock(Product.class);
                when(product.getCompany()).thenReturn(company);

                ProductOption productOption = org.mockito.Mockito.mock(ProductOption.class);
                when(productOption.getId()).thenReturn(100L);
                ProductStock stockVO = org.mockito.Mockito.mock(ProductStock.class);
                when(productOption.getProductStock()).thenReturn(stockVO);
                when(product.getOptions()).thenReturn(List.of(productOption));

                when(productRepository.findById(dto.productId())).thenReturn(Optional.of(product));
                when(productRepository.save(product)).thenReturn(product);

                IncreaseStockResponse expectedResponse = IncreaseStockResponse.builder()
                    .productId(10L)
                    .optionId(100L)
                    .increaseStockAmount(3L)
                    .build();
                when(productMapper.toIncreaseStockResponse(product, productOption,
                    dto.increaseStockAmount()))
                    .thenReturn(expectedResponse);

                // when
                IncreaseStockResponse response = productServiceImpl.increaseStock(dto);

                // then
                assertThat(response).isNotNull();
                assertThat(response.productId()).isEqualTo(10L);
                assertThat(response.optionId()).isEqualTo(100L);
                assertThat(response.increaseStockAmount()).isEqualTo(3);
            }
        }

        @Nested
        @DisplayName("Context: 프로모션이 종료된 경우 (FINISHED)")
        class PromotionNotInProgressTestForIncrease {

            @Test
            @DisplayName("It: PromotionException.PromotionNotInProgressException 예외를 발생시킨다.")
            void testIncreaseStock_PromotionFinished() {
                // given
                IncreaseStockRequestDto dto = new IncreaseStockRequestDto(10L, 100L, 3L);

                Company company = org.mockito.Mockito.mock(Company.class);

                Promotion promotion = org.mockito.Mockito.mock(Promotion.class);
                when(promotion.getStatus()).thenReturn(PromotionStatus.FINISHED);
                when(company.getPromotion()).thenReturn(promotion);

                Product product = org.mockito.Mockito.mock(Product.class);
                when(product.getCompany()).thenReturn(company);

                when(productRepository.findById(dto.productId())).thenReturn(Optional.of(product));

                // when, then
                assertThatThrownBy(() -> productServiceImpl.increaseStock(dto))
                    .isInstanceOf(PromotionException.PromotionNotInProgressException.class);
            }
        }
    }

}