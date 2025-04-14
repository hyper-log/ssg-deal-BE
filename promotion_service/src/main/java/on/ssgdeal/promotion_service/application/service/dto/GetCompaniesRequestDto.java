package on.ssgdeal.promotion_service.application.service.dto;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record GetCompaniesRequestDto(
        String keyword,
        Pageable pageable
) {
}
