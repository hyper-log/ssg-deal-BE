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
    public Product save(Product product) {
        return jpaRepository.save(product);
    }

    @Override
    public List<Product> findAllWithDetailsByIdsAndOptionIds(List<Long> productIds,
        List<Long> optionIds) {
        return jpaRepository.findAllWithDetailsByIdsAndOptionIds(productIds, optionIds);
    }

    @Override
    public List<Product> findAllWithDetailsByCompanyIdsAndOptionIds(List<Long> companyIds,
        List<Long> optionIds) {
        return jpaRepository.findAllWithDetailsByCompanyIdsAndOptionIds(companyIds, optionIds);
    }
}
