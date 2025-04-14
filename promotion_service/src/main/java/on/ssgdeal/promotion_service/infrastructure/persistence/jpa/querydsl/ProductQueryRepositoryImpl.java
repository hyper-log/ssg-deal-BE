package on.ssgdeal.promotion_service.infrastructure.persistence.jpa.querydsl;

import on.ssgdeal.promotion_service.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ProductQueryRepositoryImpl implements ProductQueryRepository {

    @Override
    public Page<Product> search(Pageable pageable) {
        return null;
    }
}
