package on.ssgdeal.promotion_service.infrastructure.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheProductDetailJobLauncher extends QuartzJobBean {

    private final JobLauncher jobLauncher;
    private final Job cacheProductDetailJob;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("프로모션 상품 상세 배치 실행 시작");
            jobLauncher.run(cacheProductDetailJob, jobParameters);
        } catch (Exception e) {
            throw new JobExecutionException("프로모션 상품 상세 배치 실행 실패", e);
        }
    }
}