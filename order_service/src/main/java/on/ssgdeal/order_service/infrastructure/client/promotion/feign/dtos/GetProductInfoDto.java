package on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos;

import java.util.List;

public record GetProductInfoDto(List<CompanyInfo> companyList) {

    public static GetProductInfoDto from(List<CompanyInfo> companyList) {
        return new GetProductInfoDto(companyList);
    }

    public record CompanyInfo(
        Long companyId,
        String companyName,
        List<CompanyProduct> companyProductList
    ) {

    }

    public record CompanyProduct(
        String promotionStatus,
        Long productId,
        String productName,
        String productPreview,
        Long originalPrice,
        Long promotionPrice,
        Long optionId,
        String optionName,
        Long extraPrice,
        Long decreaseStockAmount
    ) {

    }

}
