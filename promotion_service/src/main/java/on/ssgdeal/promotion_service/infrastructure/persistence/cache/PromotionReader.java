package on.ssgdeal.promotion_service.infrastructure.persistence.cache;

import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.infrastructure.persistence.jpa.PromotionJpaRepository;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Collections;

@Configuration
public class PromotionReader {
    private static int PROMOTION_PAGE_SIZE = 100;

    @Bean
    @StepScope
    public RepositoryItemReader<Promotion> promotionItemReader(
            PromotionJpaRepository promotionJpaRepository
    ) {
        RepositoryItemReader<Promotion> reader = new RepositoryItemReader<>();
        reader.setName("promotionItemReader");
        reader.setRepository(promotionJpaRepository);
        reader.setMethodName("findByStartPromotionDate");
        reader.setArguments(Collections.singletonList(LocalDate.now().plusDays(1)));
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        reader.setPageSize(PROMOTION_PAGE_SIZE);
        return reader;
    }
}
