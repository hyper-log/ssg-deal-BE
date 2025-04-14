package on.ssgdeal.promotion_service.presentation.dto;

import on.ssgdeal.promotion_service.application.service.dto.GetCompaniesRequestDto;
import org.springframework.data.domain.Pageable;

public record GetCompaniesRequest(
        String keyword
) {
    public static GetCompaniesRequestDto toDto(GetCompaniesRequest request, Pageable pageable) {
        return GetCompaniesRequestDto.builder()
                .keyword(request.keyword())
                .pageable(pageable)
                .build();
    }
}
