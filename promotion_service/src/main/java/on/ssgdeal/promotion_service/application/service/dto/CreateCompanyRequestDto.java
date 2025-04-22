package on.ssgdeal.promotion_service.application.service.dto;

import lombok.Builder;
import on.ssgdeal.promotion_service.domain.entity.dto.CreateCompanyDto;

@Builder
public record CreateCompanyRequestDto(
        Long managerId,
        String companyName,
        String logoUrl
) {
    public static CreateCompanyDto toDto(CreateCompanyRequestDto requestDto) {
        return CreateCompanyDto.builder()
                .managerId(requestDto.managerId())
                .companyName(requestDto.companyName())
                .logoUrl(requestDto.logoUrl())
                .build();
    }
}
