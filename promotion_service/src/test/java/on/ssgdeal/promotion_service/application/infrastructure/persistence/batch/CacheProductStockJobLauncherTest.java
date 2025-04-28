package on.ssgdeal.promotion_service.application.infrastructure.persistence.batch;

import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.promotion_service.configuration.QuartzConfig;
import on.ssgdeal.promotion_service.domain.entity.Company;
import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.ProductOption;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.dto.CreateCompanyDto;
import on.ssgdeal.promotion_service.domain.entity.dto.CreateProductDto;
import on.ssgdeal.promotion_service.domain.entity.dto.CreateProductOptionDto;
import on.ssgdeal.promotion_service.domain.entity.dto.CreatePromotionDto;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import on.ssgdeal.promotion_service.domain.repository.ProductRepository;
import on.ssgdeal.promotion_service.domain.repository.PromotionRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
@Import(QuartzConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CacheProductStockJobLauncherTest {

    @MockitoBean
    private AuditorAware<Long> auditorAware;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    @Qualifier("cacheProductStockJobDetail")
    private JobDetail cacheProductStockJob;

    @Autowired
    @Qualifier("cacheProductStockJobTrigger")
    private Trigger cacheProductStockTrigger;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private ProductRepository productRepository;
    private static final String PRODUCT_STOCK_KEY_PATTERN = "product:%d:option:%d";
    private final List<String> deleteKeys = new ArrayList<>();
    public List<CreatePromotionDto> createTestPromotions() {
        return List.of(
                CreatePromotionDto.builder()
                        .title("프로모션 타이틀 1")
                        .previewUrl("https://example.com/image1.jpg")
                        .contentImageUrl("https://example.com/image1.jpg")
                        .content("프로모션 내용")
                        .status(PromotionStatus.IN_PROGRESS)
                        .startPromotionDate(LocalDate.now().plusDays(1))
                        .endPromotionDate(LocalDate.now().plusDays(10))
                        .companyDto(
                                CreateCompanyDto.builder()
                                        .managerId(2000L)
                                        .logoUrl("https://example.com/image3.jpg")
                                        .companyName("스프링 스토어")
                                        .build()
                        )
                        .build(),

                CreatePromotionDto.builder()
                        .title("프로모션 타이틀 2")
                        .previewUrl("https://example.com/image3.jpg")
                        .contentImageUrl("https://example.com/image3.jpg")
                        .content("프로모션 내용")
                        .status(PromotionStatus.IN_PROGRESS)
                        .startPromotionDate(LocalDate.now().plusDays(1))
                        .endPromotionDate(LocalDate.now().plusDays(3))
                        .companyDto(
                                CreateCompanyDto.builder()
                                        .managerId(2000L)
                                        .logoUrl("https://example.com/image3.jpg")
                                        .companyName("가구 스토어")
                                        .build()
                        )
                        .build()
        );
    }

    public List<CreateProductDto> createProductDtos(Company company) {
        return List.of(
                CreateProductDto.builder()
                        .productName("상품 이름 1")
                        .originalPrice(10000L)
                        .promotionPrice(5000L)
                        .previewUrl("https://example.com/image3.jpg")
                        .contentImgUrl("https://example.com/image3.jpg")
                        .content("상품 설명 1")
                        .company(company)
                        .options(
                                List.of(
                                        CreateProductOptionDto.builder()
                                                .productStock(100L)
                                                .optionName("옵션 1")
                                                .extraPrice(0L)
                                                .build(),
                                        CreateProductOptionDto.builder()
                                                .productStock(100L)
                                                .optionName("옵션 2")
                                                .extraPrice(1000L)
                                                .build()
                                )
                        )
                        .build(),
                CreateProductDto.builder()
                        .productName("상품 이름 2")
                        .originalPrice(10000L)
                        .promotionPrice(5000L)
                        .previewUrl("https://example.com/image3.jpg")
                        .contentImgUrl("https://example.com/image3.jpg")
                        .content("상품 설명 2")
                        .company(company)
                        .options(
                                List.of(
                                        CreateProductOptionDto.builder()
                                                .productStock(100L)
                                                .optionName("옵션 1")
                                                .extraPrice(0L)
                                                .build(),
                                        CreateProductOptionDto.builder()
                                                .productStock(80L)
                                                .optionName("옵션 2")
                                                .extraPrice(1000L)
                                                .build(),
                                        CreateProductOptionDto.builder()
                                                .productStock(50L)
                                                .optionName("옵션 3")
                                                .extraPrice(2000L)
                                                .build()
                                )
                        )
                        .build()
        );

    }

    @BeforeAll
    void setUp() {
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of(1000L));
        List<CreatePromotionDto> mockPromotionDtos = createTestPromotions();
        List<Promotion> promotions = mockPromotionDtos.stream()
                .map(Promotion::create)
                .toList();
        List<Promotion> mockPromotions = promotionRepository.saveAll(promotions);

        for (Promotion promotion : mockPromotions) {
            Company company = promotion.getCompany();
            List<CreateProductDto> mockProductDtos = createProductDtos(company);
            List<Product> products = mockProductDtos.stream()
                    .map(Product::create)
                    .toList();
            productRepository.saveAll(products);
        }

    }

    @BeforeEach
    public void clearAllTriggers() throws SchedulerException {
        for (String groupName : scheduler.getTriggerGroupNames()) {
            for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName))) {
                scheduler.unscheduleJob(triggerKey);
            }
        }
    }

    @AfterAll
    void cleanUp() {
        redisTemplate.delete(deleteKeys);
        productRepository.deleteAll();
        promotionRepository.deleteAll();
    }

    @Nested
    @DisplayName("Describe: CacheProductStockJobLauncher 의 executeInternal 메서드는")
    class executeInternalTest {

        @Nested
        @DisplayName("Context: 정해진 트리거가 실행될 때")
        class executeInternalCacheProductSuccessTest {

            @Test
            @DisplayName("It: 상품 재고 정보를 캐싱한다.")
            void executeInternalTest() throws Exception {
                Trigger mockTrigger = TriggerBuilder.newTrigger()
                        .forJob(cacheProductStockJob)
                        .withIdentity("mockCacheProductStockTrigger")
                        .startAt(DateBuilder.newDate().build())
                        .build();

                scheduler.scheduleJob(mockTrigger);
                scheduler.start();

                Awaitility.await().atMost(Duration.ofSeconds(10))
                        .untilAsserted(() -> {
                            List<Promotion> promotions = promotionRepository.findAll();
                            for (Promotion promotion : promotions) {
                                List<Product> products = productRepository.findByCompanyIdWithOptions(promotion.getCompany().getId());
                                for (Product product : products) {
                                    List<ProductOption> productOptions = product.getOptions();
                                    for (ProductOption productOption : productOptions) {
                                        String key = String.format(PRODUCT_STOCK_KEY_PATTERN, product.getId(), productOption.getId());
                                        log.info("key: {}, hasKey: {}", key, redisTemplate.hasKey(key));
                                        assertThat(redisTemplate.hasKey(key)).isTrue();
                                        deleteKeys.add(key);
                                    }
                                }
                            }
                        });
            }
        }
    }
}