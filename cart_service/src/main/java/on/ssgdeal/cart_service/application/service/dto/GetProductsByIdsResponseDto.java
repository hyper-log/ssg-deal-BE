package on.ssgdeal.cart_service.application.service.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import on.ssgdeal.cart_service.application.service.dto.GetProductsByIdsResponseDto.SubCart.Product;
import on.ssgdeal.cart_service.application.service.dto.GetProductsByIdsResponseDto.SubCart.Product.Option;
import on.ssgdeal.cart_service.domain.entity.CartProduct;
import on.ssgdeal.cart_service.infrastructure.client.product.ProductServiceImpl.GetProductOptionsResponseDto;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductDetailsResponse;
import on.ssgdeal.cart_service.infrastructure.persistence.generator.RedisKeyGenerator;

public record GetProductsByIdsResponseDto(
    Long originalTotalPrice,
    Long discountTotalPrice,
    Long promotionTotalPrice,
    Long productCount,
    List<SubCart> subCarts
) {

    public record SubCart(
        String companyName,
        Long originalTotalPrice,
        Long discountTotalPrice,
        Long promotionTotalPrice,
        List<Product> products
    ) {

        public record Product(
            Long id,
            String name,
            Long selectedOptionId,
            String selectedOptionName,
            Long price,
            Long discountPrice,
            Long promotionPrice,
            Long quantity,
            String preview,
            List<Option> options
        ) {

            public record Option(
                String optionName,
                Long stock,
                Long extraPrice
            ) {


            }
        }
    }

    public static GetProductsByIdsResponseDto from(
        List<GetProductDetailsResponse> detailsResponseList,
        List<GetProductOptionsResponseDto> productOptionsResponses,
        List<CartProduct> cartProducts
    ) {
        Map<Long, List<GetProductDetailsResponse>> groupedByCompany = detailsResponseList.stream()
            .collect(Collectors.groupingBy(GetProductDetailsResponse::companyId));

        List<SubCart> subCarts = groupedByCompany.values().stream().map(
            getProductDetailsResponses -> {
                String companyName = getProductDetailsResponses.get(0).companyName();
                List<Product> products = getProductDetailsResponses.stream().map(detail -> {
                    List<Option> options = productOptionsResponses.stream()
                        .filter(optionResponse -> optionResponse.options().stream()
                            .anyMatch(option -> option.optionId().equals(detail.optionId())))
                        .flatMap(optionResponse -> optionResponse.options().stream())
                        .map(option -> new Option(
                            option.optionName(),
                            option.productStock(),
                            option.extraPrice()
                        ))
                        .toList();

                    return new Product(
                        detail.productId(),
                        detail.productName(),
                        detail.optionId(),
                        detail.optionName(),
                        detail.originalPrice(),
                        detail.originalPrice() - detail.promotionPrice(),
                        detail.promotionPrice(),
                        cartProducts.stream()
                            .filter(cartProduct -> {
                                Long productId = RedisKeyGenerator.parseProductId(
                                    cartProduct.getHashKey());
                                Long optionId = RedisKeyGenerator.parseOptionId(
                                    cartProduct.getHashKey());
                                return productId.equals(detail.productId()) &&
                                    optionId.equals(detail.optionId());
                            })
                            .findFirst()
                            .map(CartProduct::getQuantity)
                            .orElse(0L),
                        detail.productPreviewImgUrl(),
                        options
                    );
                }).toList();

                long originalTotalPrice = products.stream()
                    .mapToLong(SubCart.Product::price)
                    .sum();
                long discountTotalPrice = products.stream()
                    .mapToLong(SubCart.Product::discountPrice)
                    .sum();
                long promotionTotalPrice = products.stream()
                    .mapToLong(SubCart.Product::promotionPrice)
                    .sum();

                return new SubCart(companyName, originalTotalPrice, discountTotalPrice,
                    promotionTotalPrice, products);
            }).toList();

        long originalTotalPrice = subCarts.stream()
            .mapToLong(SubCart::originalTotalPrice)
            .sum();
        long discountTotalPrice = subCarts.stream()
            .mapToLong(SubCart::discountTotalPrice)
            .sum();
        long promotionTotalPrice = subCarts.stream()
            .mapToLong(SubCart::promotionTotalPrice)
            .sum();
        long productCount = subCarts.stream()
            .mapToLong(subCart -> subCart.products().size())
            .sum();

        return new GetProductsByIdsResponseDto(
            originalTotalPrice,
            discountTotalPrice,
            promotionTotalPrice,
            productCount,
            subCarts
        );
    }
}
