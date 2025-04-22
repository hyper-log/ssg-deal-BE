package on.ssgdeal.promotion_service.presentation.dto.mapper;

import on.ssgdeal.promotion_service.application.service.dto.CreatePromotionRequestDto;
import on.ssgdeal.promotion_service.presentation.dto.CreatePromotionRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionPresentationMapper {

    CreatePromotionRequestDto toDto(CreatePromotionRequest request);
}
