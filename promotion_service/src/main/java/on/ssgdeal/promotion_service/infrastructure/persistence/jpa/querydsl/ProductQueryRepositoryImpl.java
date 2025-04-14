package on.ssgdeal.promotion_service.infrastructure.persistence.jpa.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.QProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepositoryImpl implements ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    private QProduct product;

    @PostConstruct
    public void init() {
        product = QProduct.product;
    }

    @Override
    public Page<Product> searchWithProductName(String productName, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (productName != null && !productName.isEmpty()) {
            builder.and(product.name.value.containsIgnoreCase(productName));
        }

        List<Product> products = queryFactory
            .selectFrom(product)
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = queryFactory
            .select(product.id)
            .from(product)
            .where(builder)
            .fetchCount();

        return new PageImpl<>(products, pageable, total);
    }
}
