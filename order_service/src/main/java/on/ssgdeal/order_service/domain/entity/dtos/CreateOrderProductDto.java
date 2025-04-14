package on.ssgdeal.order_service.domain.entity.dtos;

import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoDto.CompanyProduct;

public record CreateOrderProductDto(Long productId, String productName, String previewUrl,
                                    Long originalPrice, Long promotionPrice, Long optionId,
                                    String optionName, Long extraPrice, Long decreaseStockAmount,
                                    Long totalPrice, Long quantity) {

    public static CreateOrderProductDto from(CompanyProduct companyProduct, Long totalPrice) {
        return new CreateOrderProductDto(companyProduct.productId(), companyProduct.productName(),
            companyProduct.productPreview(), companyProduct.originalPrice(),
            companyProduct.promotionPrice(), companyProduct.optionId(), companyProduct.optionName(),
            companyProduct.extraPrice(), companyProduct.decreaseStockAmount(), totalPrice,
            companyProduct.decreaseStockAmount());
    }
}
