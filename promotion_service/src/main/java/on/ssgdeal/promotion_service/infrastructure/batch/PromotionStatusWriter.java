package on.ssgdeal.promotion_service.infrastructure.batch;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.infrastructure.persistence.jpa.PromotionJpaRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PromotionStatusWriter implements ItemWriter<Promotion> {

    private final PromotionJpaRepository promotionJpaRepository;

    @Override
    public void write(Chunk<? extends Promotion> chunk) throws Exception {
        List<Promotion> promotions = new ArrayList<>();
        for (Promotion promotion : chunk) {
            promotions.add(promotion);
        }
        promotionJpaRepository.saveAll(promotions);
    }
}
