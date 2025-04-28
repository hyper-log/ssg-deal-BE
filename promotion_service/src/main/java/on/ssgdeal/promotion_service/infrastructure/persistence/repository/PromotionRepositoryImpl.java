package on.ssgdeal.promotion_service.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.domain.entity.Company;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.dto.CreatePromotionDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetCompaniesConditionDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetInProgressPromotionDetailDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetPromotionsConditionDto;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import on.ssgdeal.promotion_service.domain.repository.PromotionRepository;
import on.ssgdeal.promotion_service.infrastructure.persistence.jpa.PromotionJpaRepository;
import on.ssgdeal.promotion_service.infrastructure.persistence.jpa.querydsl.PromotionQueryDslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PromotionRepositoryImpl implements PromotionRepository {

    private final PromotionJpaRepository promotionJpaRepository;
    private final PromotionQueryDslRepository promotionQueryDslRepository;

    @Override
    public Optional<Promotion> findById(Long id) {
        return promotionJpaRepository.findById(id);
    }

    @Override
    public Optional<GetInProgressPromotionDetailDto> findPromotionWithProductsById(Long promotionId, Pageable pageable) {
        return promotionQueryDslRepository.findPromotionWithProductsById(promotionId, pageable);
    }
    @Override
    public Page<Promotion> findPromotions(GetPromotionsConditionDto conditionDto) {
        return promotionQueryDslRepository.findPromotions(conditionDto);
    }
    @Override
    public Page<Company> findCompanies(GetCompaniesConditionDto conditionDto) {
        return promotionQueryDslRepository.findCompanies(conditionDto);
    }

    @Override
    public List<Promotion> findAll() {
        return promotionJpaRepository.findAll();
    }

    @Override
    public Promotion save(Promotion promotion) {
        return promotionJpaRepository.save(promotion);
    }

    @Override
    public List<Promotion> saveAll(List<Promotion> promotions) {
        return promotionJpaRepository.saveAll(promotions);
    }
    @Override
    public Optional<Promotion> findFirstByStatus(PromotionStatus status) {
        return promotionJpaRepository.findFirstByStatus(status);
    }
    @Override
    public void deleteAll() {
        promotionJpaRepository.deleteAll();
    }

    @Override
    public void deleteAll(List<Promotion> promotions) {
        promotionJpaRepository.deleteAll(promotions);
    }

    @Override
    public Page<Promotion> findByStartPromotionDate(LocalDate startPromotionDate, Pageable pageable) {
        return promotionJpaRepository.findByStartPromotionDate(startPromotionDate, pageable);
    }

    @Override
    public Page<Promotion> findByEndPromotionDate(LocalDate EndPromotionDate, Pageable pageable) {
        return promotionJpaRepository.findByEndPromotionDate(EndPromotionDate, pageable);
    }
}
