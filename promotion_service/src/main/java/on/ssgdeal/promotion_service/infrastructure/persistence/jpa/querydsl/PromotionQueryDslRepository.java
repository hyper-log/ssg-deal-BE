package on.ssgdeal.promotion_service.infrastructure.persistence.jpa.querydsl;

import on.ssgdeal.promotion_service.domain.entity.Company;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.dto.GetCompaniesConditionDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetInProgressPromotionDetailDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetPromotionsConditionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PromotionQueryDslRepository {
    Optional<GetInProgressPromotionDetailDto> findPromotionWithProductsById(Long promotionId, Pageable pageable);
    Page<Promotion> findPromotions(GetPromotionsConditionDto conditionDto);
    Page<Company> findCompanies(GetCompaniesConditionDto conditionDto);
}
