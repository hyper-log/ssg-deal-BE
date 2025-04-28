package on.ssgdeal.promotion_service.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreatePromotionRequest(
        @NotBlank String title,
        @NotBlank String previewUrl,
        @NotBlank String contentImageUrl,
        @NotBlank String content,
        @NotNull String status,
        @NotNull LocalDate startPromotionDate,
        @NotNull LocalDate endPromotionDate,
        @Valid @NotNull CreateCompanyRequest companyDto
) {
}
