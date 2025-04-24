package on.ssgdeal.promotion_service.domain.repository;

import on.ssgdeal.promotion_service.domain.entity.Company;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.dto.GetCompaniesConditionDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetInProgressPromotionDetailDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetPromotionsConditionDto;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PromotionRepository {

    Optional<Promotion> findById(Long id);
    Optional<GetInProgressPromotionDetailDto> findPromotionWithProductsById(Long id, Pageable pageable);
    Page<Promotion> findPromotions(GetPromotionsConditionDto conditionDto);
    Page<Company> findCompanies(GetCompaniesConditionDto conditionDto);
    Promotion save(Promotion promotion);
    List<Promotion> saveAll(List<Promotion> promotions);
    Optional<Promotion> findFirstByStatus(PromotionStatus status);
    void deleteAll();
    Page<Promotion> findByStartPromotionDate(LocalDate startPromotionDate, Pageable pageable);

}
