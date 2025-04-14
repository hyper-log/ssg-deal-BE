package on.ssgdeal.promotion_service.application.service;

import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.promotion_service.application.service.dto.*;
import org.springframework.data.domain.Pageable;

public interface PromotionService {
    GetFinishedPromotionDetailResponseDto getFinishedPromotionDetail(Long promotionId);
    GetInProgressPromotionDetailResponseDto getInProgressPromotionDetail(Long promotionId, Pageable pageable);
    PageDto<GetPromotionsResponseDto> getPromotions(GetPromotionsRequestDto requestDto);
    PageDto<GetCompaniesResponseDto> getCompanies(GetCompaniesRequestDto requestDto);
}
