package on.ssgdeal.promotion_service.presentation.dto;

import on.ssgdeal.promotion_service.application.service.dto.GetPromotionsRequestDto;
import org.springframework.data.domain.Pageable;

public record GetPromotionsRequest(
        String keyword,
        String filter
) {
    private static final String DEFAULT_FILTER = "IN_PROGRESS";

    public static GetPromotionsRequestDto toDto(GetPromotionsRequest request, Pageable pageable) {

        return GetPromotionsRequestDto.builder()
                .filter(getOrDefaultFilter(request.filter))
                .keyword(request.keyword)
                .pageable(pageable)
                .build();
    }
    private static String getOrDefaultFilter(String filter) {
        return filter == null || filter.isEmpty() ? DEFAULT_FILTER : filter;
    }
}
