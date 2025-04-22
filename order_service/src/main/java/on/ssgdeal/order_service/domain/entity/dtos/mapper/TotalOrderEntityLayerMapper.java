package on.ssgdeal.order_service.domain.entity.dtos.mapper;

import on.ssgdeal.order_service.application.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.application.dto.UpdateCancelOrderSuccessRequestDto;
import on.ssgdeal.order_service.application.dto.UpdateTotalOrderSuccessRequestDto;
import on.ssgdeal.order_service.domain.entity.dtos.GetTotalOrdersUserInfoDto;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateCancelOrderSuccessDto;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TotalOrderEntityLayerMapper {

    UpdateTotalOrderSuccessDto toUpdateTotalOrderSuccessDto(
        UpdateTotalOrderSuccessRequestDto requestDto);

    GetTotalOrdersUserInfoDto toGetTotalOrdersUserInfoDto(
        LoginUserInfoDto loginUserInfoDto);

    UpdateCancelOrderSuccessDto toUpdateCancelOrderSuccessDto(
        UpdateCancelOrderSuccessRequestDto requestDto);

}