package on.ssgdeal.promotion_service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.promotion_service.application.service.dto.*;
import on.ssgdeal.promotion_service.application.service.dto.mapper.PromotionApplicationMapper;
import on.ssgdeal.promotion_service.domain.entity.Company;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.dto.CreatePromotionDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetCompaniesConditionDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetInProgressPromotionDetailDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetPromotionsConditionDto;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import on.ssgdeal.promotion_service.domain.repository.PromotionRepository;
import on.ssgdeal.promotion_service.exception.PromotionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionApplicationMapper mapper;

    @Override
    @Transactional
    public CreatePromotionResponseDto createPromotion(CreatePromotionRequestDto requestDto) {
        log.info("프로모션 생성 요청 : {}", requestDto.toString());
        CreatePromotionDto dto = CreatePromotionRequestDto.toDto(requestDto);
        Promotion promotion = Promotion.create(dto);
        promotionRepository.save(promotion);
        return CreatePromotionResponseDto.from(promotion);
    }

    @Override
    public PageDto<GetCompaniesResponseDto> getCompanies(GetCompaniesRequestDto requestDto) {
        GetCompaniesConditionDto conditionDto = mapper.toConditionDto(requestDto);
        Page<Company> result = promotionRepository.findCompanies(conditionDto);
        Page<GetCompaniesResponseDto> response = result.map(GetCompaniesResponseDto::from);
        return PageDto.from(response);
    }

    @Override
    public PageDto<GetPromotionsResponseDto> getPromotions(GetPromotionsRequestDto requestDto) {
        GetPromotionsConditionDto conditionDto = GetPromotionsRequestDto.toDto(requestDto);
        Page<Promotion> result = promotionRepository.findPromotions(conditionDto);
        Page<GetPromotionsResponseDto> response = result.map(GetPromotionsResponseDto::from);
        return PageDto.from(response);
    }

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
