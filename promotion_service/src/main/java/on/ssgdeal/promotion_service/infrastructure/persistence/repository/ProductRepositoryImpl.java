package on.ssgdeal.promotion_service.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.repository.ProductRepository;
import on.ssgdeal.promotion_service.infrastructure.persistence.jpa.ProductJpaRepository;
import on.ssgdeal.promotion_service.infrastructure.persistence.jpa.querydsl.ProductQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository jpaRepository;
    private final ProductQueryRepository queryRepository;

    @Override
    public Page<Product> searchWithProductName(String productName, Pageable pageable) {
        return queryRepository.searchWithProductName(productName, pageable);
    }

    @Override
    public Slice<Product> findByCompanyId(Long companyId, Pageable pageable) {
        return jpaRepository.findByCompanyId(companyId, pageable);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Product> findWithPromotionById(Long id) {
        return jpaRepository.findWithPromotionById(id);
    }

    @Override
    public Product save(Product product) {
        return jpaRepository.save(product);
    }

    @Override
    public Product saveAndFlush(Product product) {
        return jpaRepository.saveAndFlush(product);
    }

    @Override
    public List<Product> findAllWithDetailsByIdsAndOptionIds(List<Long> productIds,
        List<Long> optionIds) {
        return jpaRepository.findAllWithDetailsByIdsAndOptionIds(productIds, optionIds);
    }

    @Override
    public List<Product> findAllWithDetailsByProductIdsAndOptionIds(List<Long> productIds,
        List<Long> optionIds) {
        return jpaRepository.findAllWithDetailsByProductIdsAndOptionIds(productIds, optionIds);
    }

    @Override
    public Optional<Product> findByProductIdAndOptionId(Long productId, Long optionId) {
        return jpaRepository.findByProductIdAndOptionId(productId, optionId);
    }
}
