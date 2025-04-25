package on.ssgdeal.promotion_service.configuration;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.infrastructure.batch.*;
import on.ssgdeal.promotion_service.infrastructure.persistence.cache.dto.CachingProductDto;
import on.ssgdeal.promotion_service.infrastructure.persistence.jpa.PromotionJpaRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ProductCacheJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final PromotionJpaRepository promotionJpaRepository;
    private final PromotionReader promotionReader;
    private final ProductProcessor productProcessor;
    private final ProductDetailToDtoProcessor productDetailToDtoProcessor;
    private final ProductDetailWriter productDetailWriter;
    private final ProductStockWriter productStockWriter;

    @Bean
    public Job cacheProductDetailJob() {
        return new JobBuilder("cacheProductDetailJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(cacheProductDetailStep())
                .build();
    }

    @Bean
    public Job cacheProductStockJob() {
        return new JobBuilder("cacheProductStockJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(cacheProductStockStep())
                .build();
    }

    @Bean
    public Step cacheProductDetailStep() {
        return new StepBuilder("cacheProductDetailStep", jobRepository)
                .<Promotion, List<CachingProductDto>>chunk(10, transactionManager)
                .reader(promotionReader.promotionItemReaderByStartDate(promotionJpaRepository))
                .processor(compositeProcessor())
                .writer(productDetailWriter)
                .build();
    }

    @Bean
    public Step cacheProductStockStep() {
        return new StepBuilder("cacheProductStockStep", jobRepository)
                .<Promotion, List<Product>>chunk(10, transactionManager)
                .reader(promotionReader.promotionItemReaderByStartDate(promotionJpaRepository))
                .processor(productProcessor)
                .writer(productStockWriter)
                .build();
    }

    @Bean
    public CompositeItemProcessor<Promotion, List<CachingProductDto>> compositeProcessor() {
        CompositeItemProcessor<Promotion, List<CachingProductDto>> compositeProcessor =
                new CompositeItemProcessor<>();
        List<ItemProcessor<?, ?>> delegates = new ArrayList<>();
        delegates.add(productProcessor);
        delegates.add(productDetailToDtoProcessor);
        compositeProcessor.setDelegates(delegates);
        return compositeProcessor;
    }

    @Bean
    public JobLauncher jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

}

