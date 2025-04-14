package on.ssgdeal.promotion_service.application.service.dto;

import lombok.Builder;
import on.ssgdeal.promotion_service.domain.entity.Company;

@Builder
public record GetCompaniesResponseDto(
        Long companyId,
        String companyLogoUrl,
        String companyName
) {
    public static GetCompaniesResponseDto from(Company company) {
        return GetCompaniesResponseDto.builder()
                .companyId(company.getId())
                .companyLogoUrl(company.getLogoUrl().getValue())
                .companyName(company.getName().getValue())
                .build();
    }
}
