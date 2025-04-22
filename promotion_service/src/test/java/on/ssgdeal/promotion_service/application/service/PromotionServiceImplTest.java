package on.ssgdeal.promotion_service.application.service;

import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.promotion_service.application.service.dto.*;
import on.ssgdeal.promotion_service.domain.entity.Company;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.dto.CreateCompanyDto;
import on.ssgdeal.promotion_service.domain.entity.dto.CreatePromotionDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetCompaniesConditionDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetPromotionsConditionDto;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import on.ssgdeal.promotion_service.domain.repository.PromotionRepository;
import on.ssgdeal.promotion_service.exception.PromotionException;
import on.ssgdeal.promotion_service.presentation.dto.CreatePromotionRequest;
import org.hibernate.AssertionFailure;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("dev")
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("PromotionServiceImpl 통합 테스트")
public class PromotionServiceImplTest {

    @MockitoBean
    private AuditorAware<Long> auditorAware;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private PromotionRepository promotionRepository;

    private List<CreatePromotionDto> mockPromotionDtos;

    public List<CreatePromotionDto> createTestPromotions() {
        return List.of(
                CreatePromotionDto.builder()
                        .title("봄맞이 세일")
                        .previewUrl("https://example.com/image1.jpg")
                        .contentImageUrl("https://example.com/image1.jpg")
                        .content("봄 시즌 프로모션 내용입니다.")
                        .status(PromotionStatus.IN_PROGRESS)
                        .startPromotionDate(LocalDate.now().minusDays(1))
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
                        .title("가을 신상품 출시")
                        .previewUrl("https://example.com/image3.jpg")
                        .contentImageUrl("https://example.com/image3.jpg")
                        .content("가을 패션, 리빙 신상품 프로모션.")
                        .status(PromotionStatus.FINISHED)
                        .startPromotionDate(LocalDate.now().minusDays(10))
                        .endPromotionDate(LocalDate.now().minusDays(3))
                        .companyDto(
                                CreateCompanyDto.builder()
                                        .managerId(2000L)
                                        .logoUrl("https://example.com/image3.jpg")
                                        .companyName("가구 스토어")
                                        .build()
                        )
                        .build(),

                CreatePromotionDto.builder()
                        .title("신년 맞이 혜택")
                        .previewUrl("https://example.com/image5.jpg")
                        .contentImageUrl("https://example.com/image5.jpg")
                        .content("새해 프로모션, 쿠폰, 포인트 지급.")
                        .status(PromotionStatus.PENDING)
                        .startPromotionDate(LocalDate.now().plusDays(10))
                        .endPromotionDate(LocalDate.now().plusDays(20))
                        .companyDto(
                                CreateCompanyDto.builder()
                                        .managerId(2000L)
                                        .logoUrl("https://example.com/image3.jpg")
                                        .companyName("식탁스토어")
                                        .build()
                        )
                        .build()
        );
    }

    @BeforeAll
    void setUp() {
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of(1000L));
        mockPromotionDtos = createTestPromotions();
        List<Promotion> promotions = mockPromotionDtos.stream()
                .map(Promotion::create)
                .toList();
        promotionRepository.saveAll(promotions);
    }

    @AfterAll
    void tearDown() {
        promotionRepository.deleteAll();
    }

    @Nested
    @DisplayName("Describe: getFinishedPromotionDetail 메서드는")
    class getFinishedPromotionDetailTest {

        @Nested
        @DisplayName("Context: 유효한 요청으로 종료된 프로모션 상세 정보를 조회할 때")
        class getFinishedPromotionDetailSuccessTest {

            @Test
            @DisplayName("It: 종료된 프로모션의 상세 정보를 반환하다.")
            void getFinishedPromotionDetailTest() throws Exception {

                //given
                Promotion finishedPromotion = promotionRepository
                        .findFirstByStatus(PromotionStatus.FINISHED)
                        .orElseThrow(PromotionException.PromotionNotFoundException::new);

                //when
                GetFinishedPromotionDetailResponseDto response = promotionService.getFinishedPromotionDetail(finishedPromotion.getId());
                Promotion promotion = promotionRepository.findById(finishedPromotion.getId())
                        .orElseThrow(PromotionException.PromotionNotFoundException::new);

                //then
                assertThat(response).isNotNull();
                assertThat(promotion.getStatus()).isEqualTo(PromotionStatus.FINISHED);
                assertThat(promotion.getId()).isEqualTo(response.promotionId());
            }
        }
    }

    @Nested
    @DisplayName("Describe: getInProgressPromotionDetail 메서드는")
    class getInProgressPromotionDetailTest {

        @Nested
        @DisplayName("Context: 유효한 요청으로 진행 중인 프로모션 상세 정보를 조회할 때")
        class getFinishedPromotionDetailSuccessTest {

            @Test
            @DisplayName("It: 진행 중인 프로모션의 상세 정보를 반환하다.")
            void getFinishedPromotionDetailTest() throws Exception {

                //given
                Promotion promotion = promotionRepository
                        .findFirstByStatus(PromotionStatus.IN_PROGRESS)
                        .orElseThrow(PromotionException.PromotionNotFoundException::new);
                Pageable pageable = PageRequest.of(0, 10);

                //when
                GetInProgressPromotionDetailResponseDto response = promotionService.getInProgressPromotionDetail(promotion.getId(), pageable);
                Promotion inProgressPromotion = promotionRepository.findById(promotion.getId())
                        .orElseThrow(PromotionException.PromotionNotFinishedException::new);

                //then
                assertThat(response).isNotNull();
                assertThat(inProgressPromotion.getStatus()).isEqualTo(PromotionStatus.IN_PROGRESS);
                assertThat(inProgressPromotion.getId()).isEqualTo(response.promotionId());
                assertThat(response.promotionProducts()).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("Describe: getPromotions 메서드는")
    class getPromotionsTest {

        @Nested
        @DisplayName("Context: 유효한 요청으로 프로모션 리스트를 조회할 때")
        class getPromotionsSuccessTest {

            @Test
            @DisplayName("It: 조건에 맞는 프로모션 리스트를 반환하다.")
            void getPromotionsTest() throws Exception {

                //given
                Pageable pageable = PageRequest.of(0, 10);
                GetPromotionsConditionDto conditionDto = GetPromotionsConditionDto.builder()
                        .filter(PromotionStatus.FINISHED)
                        .pageable(pageable)
                        .build();

                GetPromotionsRequestDto requestDto = GetPromotionsRequestDto.builder()
                        .filter("FINISHED")
                        .pageable(pageable)
                        .build();

                //when
                PageDto<GetPromotionsResponseDto> response = promotionService.getPromotions(requestDto);
                Page<Promotion> result = promotionRepository.findPromotions(conditionDto);
                Page<GetPromotionsResponseDto> expectedResponse = result.map(GetPromotionsResponseDto::from);

                //then
                assertThat(response).isNotNull();
                assertThat(result.getContent().get(0).getStatus()).isEqualTo(PromotionStatus.FINISHED);
                assertThat(expectedResponse.get().count()).isEqualTo(response.content().size());
            }
        }
    }

    @Nested
    @DisplayName("Describe: getCompanies 메서드는")
    class getCompaniesTest {

        @Nested
        @DisplayName("Context: 유효한 요청으로 업체 리스트를 조회할 때")
        class getCompaniesSuccessTest {

            @Test
            @DisplayName("It: 조건에 맞는 업체 리스트를 반환하다.")
            void getCompaniesTest() throws Exception {

                //given
                Pageable pageable = PageRequest.of(0, 10);
                GetCompaniesConditionDto conditionDto = GetCompaniesConditionDto.builder()
                        .keyword("스")
                        .pageable(pageable)
                        .build();

                GetCompaniesRequestDto requestDto = GetCompaniesRequestDto.builder()
                        .keyword("스")
                        .pageable(pageable)
                        .build();

                //when
                PageDto<GetCompaniesResponseDto> response = promotionService.getCompanies(requestDto);
                Page<Company> result = promotionRepository.findCompanies(conditionDto);
                Page<GetCompaniesResponseDto> expectedResponse = result.map(GetCompaniesResponseDto::from);

                //then
                assertThat(response).isNotNull();
                assertThat(response.content().get(0).companyName()).contains("스");
                assertThat(expectedResponse.get().count()).isEqualTo(response.content().size());
            }
        }
    }

    @Nested
    @DisplayName("Describe: createPromotion 메서드는")
    class createPromotionTest {

        @Nested
        @DisplayName("Context: 유효한 요청으로 프로모션 생성을 요청할 때")
        class getCompaniesSuccessTest {

            @Test
            @DisplayName("It: 프로모션과 업체를 생성하고 프로모션 ID를 반환한다.")
            void getCompaniesTest() throws Exception {

                //given
                CreatePromotionRequestDto requestDto = CreatePromotionRequestDto.builder()
                        .title("여름 특가 세일")
                        .previewUrl("https://example.com/preview.jpg")
                        .contentImageUrl("https://example.com/content.jpg")
                        .content("이번 여름 프로모션으로 다양한 할인 혜택을 만나보세요!")
                        .status("IN_PROGRESS")
                        .startPromotionDate(LocalDate.now().minusDays(2))
                        .endPromotionDate(LocalDate.now().plusDays(3))
                        .companyDto(
                                CreateCompanyRequestDto.builder()
                                        .managerId(2000L)
                                        .logoUrl("https://example.com/image3.jpg")
                                        .companyName("화장품 스토어")
                                        .build()
                        )
                        .build();

                //when
                CreatePromotionResponseDto response = promotionService.createPromotion(requestDto);
                Promotion result = promotionRepository.findById(response.promotionId())
                        .orElseThrow(PromotionException.PromotionNotFoundException::new);

                //then
                assertThat(response).isNotNull();
                assertThat(response.promotionId()).isEqualTo(result.getId());
            }
        }
    }

}