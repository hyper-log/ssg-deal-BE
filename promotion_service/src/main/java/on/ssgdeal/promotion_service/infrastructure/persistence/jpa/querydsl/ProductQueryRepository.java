package on.ssgdeal.promotion_service.infrastructure.persistence.jpa.querydsl;

import on.ssgdeal.promotion_service.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryRepository {

    Page<Product> searchWithProductName(String productName, Pageable pageable);


}
