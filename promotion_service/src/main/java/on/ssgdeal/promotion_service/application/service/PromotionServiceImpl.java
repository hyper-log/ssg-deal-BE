package on.ssgdeal.promotion_service.application.service;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.application.service.dto.GetFinishedPromotionDetailResponseDto;
import on.ssgdeal.promotion_service.application.service.dto.GetInProgressPromotionDetailResponseDto;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.dto.GetInProgressPromotionDetailDto;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import on.ssgdeal.promotion_service.domain.repository.PromotionRepository;
import on.ssgdeal.promotion_service.exception.PromotionException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    @Override
    public GetFinishedPromotionDetailResponseDto getFinishedPromotionDetail(Long promotionId) {
        Promotion promotion = getPromotionOrThrow(promotionId);
        validatePromotionIsFinished(promotion);
        return GetFinishedPromotionDetailResponseDto.from(promotion);
    }

    private Promotion getPromotionOrThrow(Long promotionId) {
        return promotionRepository.findById(promotionId)
                .orElseThrow(PromotionException.PromotionNotFoundException::new);
    }

    @Override
    public GetInProgressPromotionDetailResponseDto getInProgressPromotionDetail(
            Long promotionId,
            Pageable pageable
    ) {
        Promotion promotion = getPromotionOrThrow(promotionId);
        validatePromotionIsInProgress(promotion);
        GetInProgressPromotionDetailDto result = promotionRepository.findPromotionWithProductsById(promotionId, pageable).orElseThrow(PromotionException.PromotionNotFoundException::new);
        return GetInProgressPromotionDetailResponseDto.from(result);
    }

    private static void validatePromotionIsFinished(Promotion promotion) {
        if (promotion.getEndPromotionDate().isAfter(LocalDate.now())
                || promotion.getStatus() != PromotionStatus.FINISHED
        ) {
            throw new PromotionException.PromotionNotFinishedException();
        }
    }

    private static void validatePromotionIsInProgress(Promotion promotion) {
        if (promotion.getEndPromotionDate().isBefore(LocalDate.now())
                || promotion.getStatus() != PromotionStatus.IN_PROGRESS
        ) {
            throw new PromotionException.PromotionNotInProgressException();
        }
    }
}
