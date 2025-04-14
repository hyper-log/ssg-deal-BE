package on.ssgdeal.promotion_service.presentation.internal.dto.mapper;

import on.ssgdeal.promotion_service.application.service.dto.product.DecreaseStockRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.GetProductDetailsRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.GetProductOptionsRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.IncreaseStockRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.ValidateStockDecreasesRequestDto;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.DecreaseStockRequest;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.GetProductDetailsRequest;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.GetProductOptionsRequest;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.IncreaseStockRequest;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.ValidateStockDecreasesRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductPresentationMapper {

    DecreaseStockRequestDto toDto(DecreaseStockRequest request);

    GetProductDetailsRequestDto toDto(GetProductDetailsRequest request);

    GetProductOptionsRequestDto toDto(GetProductOptionsRequest request);

    IncreaseStockRequestDto toDto(IncreaseStockRequest request);

    @Mapping(target = "getProductDetails", source = "getProductDetails")
    ValidateStockDecreasesRequestDto toDto(ValidateStockDecreasesRequest request);
}
