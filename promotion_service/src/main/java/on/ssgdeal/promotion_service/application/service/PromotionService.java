package on.ssgdeal.promotion_service.application.service;

import on.ssgdeal.promotion_service.application.service.dto.GetFinishedPromotionDetailResponseDto;
import on.ssgdeal.promotion_service.application.service.dto.GetInProgressPromotionDetailResponseDto;
import org.springframework.data.domain.Pageable;

public interface PromotionService {
    GetFinishedPromotionDetailResponseDto getFinishedPromotionDetail(Long promotionId);
    GetInProgressPromotionDetailResponseDto getInProgressPromotionDetail(Long promotionId, Pageable pageable);

}
