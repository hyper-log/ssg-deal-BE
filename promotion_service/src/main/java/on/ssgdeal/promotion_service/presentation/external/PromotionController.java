package on.ssgdeal.promotion_service.presentation.external;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.promotion_service.application.service.PromotionService;
import on.ssgdeal.promotion_service.application.service.dto.*;
import on.ssgdeal.promotion_service.presentation.dto.CreatePromotionRequest;
import on.ssgdeal.promotion_service.presentation.dto.GetCompaniesRequest;
import on.ssgdeal.promotion_service.presentation.dto.GetPromotionsRequest;
import on.ssgdeal.promotion_service.presentation.dto.mapper.PromotionPresentationMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotions")
public class PromotionController {

    private final PromotionService promotionService;
    private final PromotionPresentationMapper mapper;

    @PostMapping
    public ResponseEntity<CommonResponse<CreatePromotionResponseDto>> createdPromotion(
            @RequestBody final CreatePromotionRequest request,
            HttpServletRequest servletRequest
    ) {
        final var responseDto = promotionService.createPromotion(mapper.toDto(request));
        return ResponseEntity.ok().body(CommonResponse.success(responseDto));
    }

    @GetMapping("/finished/{promotionId}")
    public ResponseEntity<CommonResponse<GetFinishedPromotionDetailResponseDto>> getFinishedPromotionDetail(
            @PathVariable final Long promotionId
    ) {
        final var responseDto = promotionService.getFinishedPromotionDetail(promotionId);
        return ResponseEntity.ok().body(CommonResponse.success(responseDto));
    }

    @GetMapping("/in-progress/{promotionId}")
    public ResponseEntity<CommonResponse<GetInProgressPromotionDetailResponseDto>> getInProgressPromotionDetail(
            @PathVariable final Long promotionId,
            @RequestParam(required = false) Long lastId,
            final Pageable pageable
            ) {
        final var responseDto = promotionService.getInProgressPromotionDetail(promotionId, pageable);
        return ResponseEntity.ok().body(CommonResponse.success(responseDto));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<PageDto<GetPromotionsResponseDto>>> getPromotions(
            @ModelAttribute GetPromotionsRequest request,
            Pageable pageable
            ) {
        log.info("프로모션 리스트 조회 요청 : {} ", request.toString());
        final var responseDto = promotionService.getPromotions(GetPromotionsRequest.toDto(request, pageable));
        return ResponseEntity.ok().body(CommonResponse.success(responseDto));
    }

    @GetMapping("/companies/search")
    public ResponseEntity<CommonResponse<PageDto<GetCompaniesResponseDto>>> getCompanies(
            @ModelAttribute GetCompaniesRequest request,
            Pageable pageable
    ) {
        log.info("업체 리스트 조회 요청 : {} ", request.toString());
        final var responseDto = promotionService.getCompanies(GetCompaniesRequest.toDto(request, pageable));
        return ResponseEntity.ok().body(CommonResponse.success(responseDto));
    }
}


