package on.ssgdeal.promotion_service.application.service.dto.mapper;

import on.ssgdeal.promotion_service.application.service.dto.GetCompaniesRequestDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetCompaniesConditionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionApplicationMapper {
    GetCompaniesConditionDto toConditionDto(GetCompaniesRequestDto requestDto);
}
