package on.ssgdeal.order_service.application.service.dto;

import java.util.List;

public record TotalOrderProductInfo(List<ProductInfo> products) {

    public static TotalOrderProductInfo from(List<ProductInfo> products) {
        return new TotalOrderProductInfo(products);
    }

    public record ProductInfo(Long productId, Long optionId, Long quantity) {

    }
}
