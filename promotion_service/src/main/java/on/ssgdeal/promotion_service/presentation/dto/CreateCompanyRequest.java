package on.ssgdeal.promotion_service.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCompanyRequest(
        @NotNull Long managerId,
        @NotBlank String companyName,
        @NotBlank String logoUrl
) {
}
