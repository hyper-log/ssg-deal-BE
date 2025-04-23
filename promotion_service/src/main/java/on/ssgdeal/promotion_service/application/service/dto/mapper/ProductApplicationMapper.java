package on.ssgdeal.promotion_service.application.service.dto.mapper;

import on.ssgdeal.promotion_service.application.service.dto.product.DecreaseStockRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.IncreaseStockRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.stock.UpdateStockRequestDto;
import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.ProductOption;
import on.ssgdeal.promotion_service.presentation.external.dto.product.FindByIdResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.FindByPromotionIdResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.DecreaseStockResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.IncreaseStockResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductApplicationMapper {

    @Mapping(target = "amount", source = "increaseStockAmount")
    UpdateStockRequestDto toDto(IncreaseStockRequestDto requestDto);

    @Mapping(target = "amount", source = "decreaseStockAmount")
    UpdateStockRequestDto toDto(DecreaseStockRequestDto requestDto);

    @Mapping(source = "id", target = "productId")
    @Mapping(source = "name.value", target = "productName")
    @Mapping(source = "company.name.value", target = "companyName")
    @Mapping(source = "previewUrl.value", target = "productPreviewImgUrl")
    @Mapping(source = "originalPrice.value", target = "originalPrice")
    @Mapping(source = "promotionPrice.value", target = "promotionPrice")
    @Mapping(source = "contentImgUrl.value", target = "productContentUrl")
    @Mapping(source = "content", target = "productContent")
    FindByIdResponse toFindByIdResponse(Product product);

    @Mapping(source = "optionName.value", target = "optionName")
    @Mapping(source = "productStock.value", target = "stock")
    @Mapping(source = "extraPrice.value", target = "extraPrice")
    FindByIdResponse.Options toFindByIdResponseOptions(ProductOption productOption);

    @Mapping(source = "id", target = "productId")
    @Mapping(source = "name.value", target = "productName")
    @Mapping(source = "company.name.value", target = "companyName")
    @Mapping(source = "previewUrl.value", target = "productPreviewImgUrl")
    @Mapping(source = "originalPrice.value", target = "originalPrice")
    @Mapping(source = "promotionPrice.value", target = "promotionPrice")
    FindByPromotionIdResponse toFindByPromotionIdResponse(Product product);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "productOption.id", target = "optionId")
    @Mapping(source = "decreaseAmount", target = "decreaseStockAmount")
    DecreaseStockResponse toDecreaseStockResponse(
        Product product,
        ProductOption productOption,
        Long decreaseAmount
    );

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "productOption.id", target = "optionId")
    @Mapping(source = "increaseAmount", target = "increaseStockAmount")
    IncreaseStockResponse toIncreaseStockResponse(
        Product product,
        ProductOption productOption,
        Long increaseAmount
    );
}
