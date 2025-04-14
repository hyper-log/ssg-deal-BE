package on.ssgdeal.promotion_service.application.service;

import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.promotion_service.application.service.dto.*;
import on.ssgdeal.promotion_service.domain.entity.Company;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.dto.GetCompaniesConditionDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetPromotionsConditionDto;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import on.ssgdeal.promotion_service.domain.repository.PromotionRepository;
import org.hibernate.AssertionFailure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("dev")
@Transactional
@SpringBootTest
@DisplayName("PromotionServiceImpl 통합 테스트")
public class PromotionServiceImplTest {

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private PromotionRepository promotionRepository;

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
                Long requestPromotionId = 1L;

                //when
                GetFinishedPromotionDetailResponseDto response = promotionService.getFinishedPromotionDetail(requestPromotionId);
                Promotion promotion = promotionRepository.findById(requestPromotionId).orElseThrow(
                        () -> new AssertionFailure("프로모션이 조회되지 않았습니다.")
                );

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
                Long requestPromotionId = 2L;
                Pageable pageable = PageRequest.of(0, 10);

                //when
                GetInProgressPromotionDetailResponseDto response = promotionService.getInProgressPromotionDetail(requestPromotionId, pageable);
                Promotion promotion = promotionRepository.findById(requestPromotionId).orElseThrow(
                        () -> new AssertionFailure("프로모션이 조회되지 않았습니다.")
                );

                //then
                assertThat(response).isNotNull();
                assertThat(promotion.getStatus()).isEqualTo(PromotionStatus.IN_PROGRESS);
                assertThat(promotion.getId()).isEqualTo(response.promotionId());
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
}