package on.ssgdeal.promotion_service.configuration;

import on.ssgdeal.promotion_service.infrastructure.batch.AutowiringSpringBeanJobFactory;
import on.ssgdeal.promotion_service.infrastructure.batch.CacheProductDetailJobLauncher;
import on.ssgdeal.promotion_service.infrastructure.batch.CacheProductStockJobLauncher;
import on.ssgdeal.promotion_service.infrastructure.batch.PromotionFinishedStatusJobLauncher;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class QuartzConfig {

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, AutowiringSpringBeanJobFactory jobFactory) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        factory.setOverwriteExistingJobs(true);
        factory.setStartupDelay(5);
        factory.setJobDetails(cacheProductDetailJobDetail(), cacheProductStockJobDetail(), promotionFinishedStatusJobDetail());
        factory.setTriggers(cacheProductDetailJobTrigger(), cacheProductStockJobTrigger(), promotionFinishedStatusJobTrigger());
        return factory;
    }

    @Bean
    public Scheduler scheduler(SchedulerFactoryBean factory) {
        return factory.getScheduler();
    }

    @Bean
    @Qualifier("promotionFinishedStatusJobDetail")
    public JobDetail promotionFinishedStatusJobDetail() {
        return JobBuilder.newJob(PromotionFinishedStatusJobLauncher.class)
                .withIdentity("promotionFinishedStatusJob")
                .storeDurably()
                .build();
    }

    @Bean
    @Qualifier("cacheProductDetailJobDetail")
    public JobDetail cacheProductDetailJobDetail() {
        return JobBuilder.newJob(CacheProductDetailJobLauncher.class)
                .withIdentity("cacheProductDetailJob")
                .storeDurably()
                .build();
    }

    @Bean
    @Qualifier("cacheProductStockJobDetail")
    public JobDetail cacheProductStockJobDetail() {
        return JobBuilder.newJob(CacheProductStockJobLauncher.class)
                .withIdentity("cacheProductStockJob")
                .storeDurably()
                .build();
    }

    @Bean
    @Qualifier("promotionFinishedStatusJobTrigger")
    public Trigger promotionFinishedStatusJobTrigger() {

        return TriggerBuilder.newTrigger()
                .forJob(promotionFinishedStatusJobDetail())
                .withIdentity("promotionFinishedStatusJobTrigger")
                .withSchedule(
                        CronScheduleBuilder.cronSchedule("0 0 0 * * ?")
                )
                .build();
    }

    @Bean
    @Qualifier("cacheProductDetailJobTrigger")
    public Trigger cacheProductDetailJobTrigger() {

        return TriggerBuilder.newTrigger()
                .forJob(cacheProductDetailJobDetail())
                .withIdentity("cacheProductDetailTrigger")
                .withSchedule(
                        CronScheduleBuilder.cronSchedule("0 30 23 * * ?")
                )
                .build();
    }

    @Bean
    @Qualifier("cacheProductStockJobTrigger")
    public Trigger cacheProductStockJobTrigger() {

        return TriggerBuilder.newTrigger()
                .forJob(cacheProductStockJobDetail())
                .withIdentity("cacheProductStockTrigger")
                .withSchedule(
                        CronScheduleBuilder.cronSchedule("0 30 23 * * ?")
                )
                .build();
    }

    @Bean
    public AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

}