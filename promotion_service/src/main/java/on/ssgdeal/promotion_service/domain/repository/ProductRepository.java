package on.ssgdeal.promotion_service.domain.repository;

import java.util.List;
import java.util.Optional;
import on.ssgdeal.promotion_service.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductRepository {

    Page<Product> searchWithProductName(String productName, Pageable pageable);

    Slice<Product> findByCompanyId(Long companyId, Pageable pageable);

    Optional<Product> findById(Long id);

    Optional<Product> findWithPromotionById(Long id);

    Product save(Product product);

    Product saveAndFlush(Product product);

    List<Product> findAllWithDetailsByIdsAndOptionIds(List<Long> productIds, List<Long> optionIds);

    List<Product> findAllWithDetailsByProductIdsAndOptionIds(List<Long> productIds,
        List<Long> optionIds);

    Optional<Product> findByProductIdAndOptionId(Long productId, Long optionId);
}
