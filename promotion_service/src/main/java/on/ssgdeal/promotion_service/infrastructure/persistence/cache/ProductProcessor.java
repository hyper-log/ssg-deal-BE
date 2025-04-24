package on.ssgdeal.promotion_service.infrastructure.persistence.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.exception.PromotionException;
import on.ssgdeal.promotion_service.infrastructure.persistence.jpa.ProductJpaRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductProcessor implements ItemProcessor<Promotion, List<Product>> {
    private static final int PRODUCT_PAGE_SIZE = 100;
    private final ProductJpaRepository productJpaRepository;

    @Override
    public List<Product> process(Promotion promotion) throws Exception {
        if (promotion == null || promotion.getCompany() == null) {
            return Collections.emptyList();
        }

        Long companyId = promotion.getCompany().getId();
        log.info("업체 ID: {}", companyId);
        List<Product> products = new ArrayList<>();
        int page = 0;
        Page<Product> productPage;

        try {
            do {
                productPage = productJpaRepository.findPageByCompanyId(companyId, PageRequest.of(page, PRODUCT_PAGE_SIZE));
                products.addAll(productPage.getContent());
                page++;
            } while (productPage.hasNext());

            return products;
        } catch (Exception e) {
            log.error("프로모션의 상품 배치 처리 실패");
            throw new PromotionException.ItemProcessingException();
        }
    }

}