package on.ssgdeal.promotion_service.application.service;

import on.ssgdeal.promotion_service.application.service.dto.GetFinishedPromotionDetailResponseDto;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.repository.PromotionRepository;
import org.hibernate.AssertionFailure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        @DisplayName("Context: 유효한 요청으로 종료된 프로모션 상세 정보를 반환하다.")
        class getFinishedPromotionDetailSuccessTest {

            @Test
            @DisplayName("It: 종료된 프로모션 상세 정보를 조회")
            void getFinishedPromotionDetailTest() throws Exception {

                //given
                Long requestPromotionId = 1L;

                //when
                GetFinishedPromotionDetailResponseDto response = promotionService.getFinishedPromotionDetail(requestPromotionId);
                Promotion promotion = promotionRepository.findById(requestPromotionId).orElseThrow(
                    () -> new AssertionFailure("Promotion이 조회되지 않았습니다.")
                );

                //then
                assertThat(response).isNotNull();
                assertThat(promotion.getId()).isEqualTo(response.promotionId());

            }
        }
    }
}





