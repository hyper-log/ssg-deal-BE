package on.ssgdeal.promotion_service.application.service.dto;

import lombok.Builder;
import on.ssgdeal.promotion_service.domain.entity.dto.CreatePromotionDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetPromotionsConditionDto;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;

import java.time.LocalDate;

@Builder
public record CreatePromotionRequestDto(
        String title,
        String previewUrl,
        String contentImageUrl,
        String content,
        String status,
        LocalDate startPromotionDate,
        LocalDate endPromotionDate,
        CreateCompanyRequestDto companyDto
) {
    public static CreatePromotionDto toDto(CreatePromotionRequestDto requestDto) {
        return CreatePromotionDto.builder()
                .title(requestDto.title())
                .previewUrl(requestDto.previewUrl())
                .contentImageUrl(requestDto.contentImageUrl())
                .content(requestDto.content())
                .status(PromotionStatus.from(requestDto.status()))
                .startPromotionDate(requestDto.startPromotionDate())
                .endPromotionDate(requestDto.endPromotionDate())
                .companyDto(CreateCompanyRequestDto.toDto(requestDto.companyDto()))
                .build();
    }

}
