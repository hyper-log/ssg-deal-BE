package on.ssgdeal.promotion_service.application.infrastructure.persistence.batch;

import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.promotion_service.configuration.QuartzConfig;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.dto.CreateCompanyDto;
import on.ssgdeal.promotion_service.domain.entity.dto.CreatePromotionDto;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
@Import(QuartzConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PromotionFinishedStatusJobLauncherTest {

    @MockitoBean
    private AuditorAware<Long> auditorAware;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    @Qualifier("promotionFinishedStatusJobDetail")
    private JobDetail promotionFinishedStatusJob;

    @Autowired
    @Qualifier("promotionFinishedStatusJobTrigger")
    private Trigger promotionFinishedStatusJobTrigger;

    @Autowired
    private PromotionRepository promotionRepository;
    public List<CreatePromotionDto> createTestPromotions() {
        return List.of(
                CreatePromotionDto.builder()
                        .title("프로모션 타이틀 1")
                        .previewUrl("https://example.com/image1.jpg")
                        .contentImageUrl("https://example.com/image1.jpg")
                        .content("프로모션 내용")
                        .status(PromotionStatus.IN_PROGRESS)
                        .startPromotionDate(LocalDate.now().minusDays(10))
                        .endPromotionDate(LocalDate.now().minusDays(1))
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
                        .startPromotionDate(LocalDate.now().minusDays(20))
                        .endPromotionDate(LocalDate.now().minusDays(1))
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

    @BeforeAll
    void setUp() {
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of(1000L));
        List<CreatePromotionDto> mockPromotionDtos = createTestPromotions();
        List<Promotion> promotions = mockPromotionDtos.stream()
                .map(Promotion::create)
                .toList();
        promotionRepository.saveAll(promotions);
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
        promotionRepository.deleteAll();
    }

    @Nested
    @DisplayName("Describe: PromotionFinishedStatus 의 executeInternal 메서드는")
    class executeInternalTest {

        @Nested
        @DisplayName("Context: 정해진 트리거가 실행될 때")
        class executeInternalCacheProductSuccessTest {

            @Test
            @DisplayName("It: 프로모션을 종료된 상태로 업데이트한다.")
            void executeInternalTest() throws Exception {
                Trigger mockTrigger = TriggerBuilder.newTrigger()
                        .forJob(promotionFinishedStatusJob)
                        .withIdentity("mockPromotionFinishedStatusTrigger")
                        .startAt(DateBuilder.newDate().build())
                        .build();

                scheduler.scheduleJob(mockTrigger);
                scheduler.start();

                Awaitility.await().atMost(Duration.ofSeconds(10))
                        .untilAsserted(() -> {
                            List<Promotion> promotions = promotionRepository.findAll();
                            for (Promotion promotion : promotions) {
                                assertThat(promotion.getStatus()).isEqualTo(PromotionStatus.FINISHED);
                            }
                        });
            }
        }
    }
}