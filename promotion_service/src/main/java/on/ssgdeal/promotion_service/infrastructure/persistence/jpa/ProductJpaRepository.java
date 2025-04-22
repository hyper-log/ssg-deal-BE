package on.ssgdeal.promotion_service.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import on.ssgdeal.promotion_service.domain.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.company.id = :companyId")
    Slice<Product> findByCompanyId(@Param("companyId") Long companyId, Pageable pageable);

    Optional<Product> findById(Long id);

    @Query("SELECT p FROM Product p " +
        "JOIN FETCH p.options o " +
        "JOIN FETCH p.company c " +
        "JOIN FETCH c.promotion promo " +
        "WHERE p.id IN :productIds " +
        "AND o.id IN :optionIds")
    List<Product> findAllWithDetailsByIdsAndOptionIds(
        @Param("productIds") List<Long> productIds,
        @Param("optionIds") List<Long> optionIds
    );

    @Query("SELECT p FROM Product p " +
        "JOIN FETCH p.options o " +
        "JOIN FETCH p.company c " +
        "JOIN FETCH c.promotion promo " +
        "WHERE p.id IN :productIds " +
        "AND o.id IN :optionIds")
    List<Product> findAllWithDetailsByProductIdsAndOptionIds(
        @Param("productIds") List<Long> productIds,
        @Param("optionIds") List<Long> optionIds
    );

    @Query("SELECT p FROM Product p " +
        "JOIN FETCH p.options o " +
        "WHERE p.id = :productId " +
        "AND o.id = :optionId")
    Optional<Product> findByProductIdAndOptionId(
        @Param("productId") Long productId,
        @Param("optionId") Long optionId
    );

    @Query("SELECT p FROM Product p " +
        "JOIN FETCH p.company c " +
        "JOIN FETCH c.promotion promo " +
        "WHERE p.id = :productId "
    )
    Optional<Product> findWithPromotionById(
        @Param("productId") Long id
    );
}
