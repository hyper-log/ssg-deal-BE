package on.ssgdeal.promotion_service.presentation.external;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.promotion_service.application.service.PromotionService;
import on.ssgdeal.promotion_service.application.service.dto.GetFinishedPromotionDetailResponseDto;
import on.ssgdeal.promotion_service.application.service.dto.GetInProgressPromotionDetailResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotions")
public class PromotionController {

    private final PromotionService promotionService;

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
}


