package on.ssgdeal.promotion_service.infrastructure.batch;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionStatusProcessor implements ItemProcessor<Promotion, Promotion> {

    @Override
    public Promotion process(Promotion item) throws Exception {
        if (item == null) {
            return null;
        }
        item.updateToFinishedStatus();
        return item;
    }
}
