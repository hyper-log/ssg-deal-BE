package on.ssgdeal.promotion_service.presentation.external;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotions")
public class JobLauncherController {

    private final JobLauncher jobLauncher;
    private final Job cacheProductJob;

    @PostMapping("/product/cache")
    public String handle() throws Exception{
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(cacheProductJob, jobParameters);
        return "Job Status: " + jobExecution.getStatus();
    }
}