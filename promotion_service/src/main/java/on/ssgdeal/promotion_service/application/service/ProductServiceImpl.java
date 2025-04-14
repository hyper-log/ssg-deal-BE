package on.ssgdeal.promotion_service.application.service;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.common.application.dto.SliceDto;
import on.ssgdeal.promotion_service.application.service.dto.product.DecreaseStockRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.FindProductByPromotionIdRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.GetProductDetailsRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.GetProductOptionsRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.IncreaseStockRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.ValidateStockDecreasesRequestDto;
import on.ssgdeal.promotion_service.domain.entity.Company;
import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.ProductOption;
import on.ssgdeal.promotion_service.domain.entity.ProductRanking;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.mapper.ProductMapper;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import on.ssgdeal.promotion_service.domain.repository.ProductRepository;
import on.ssgdeal.promotion_service.domain.repository.PromotionRepository;
import on.ssgdeal.promotion_service.exception.ProductException;
import on.ssgdeal.promotion_service.exception.PromotionException;
import on.ssgdeal.promotion_service.presentation.external.dto.product.FindByIdResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.FindByPromotionIdResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.GetProductRankingResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.SearchProductResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.DecreaseStockResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.GetProductDetailsResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.GetProductOptionsResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.IncreaseStockResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.ValidateStockDecreasesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final ProductMapper productMapper;


    @Override
    public PageDto<SearchProductResponse> search(Pageable pageable) {
        Page<Product> products = productRepository.search(pageable);

        return PageDto.from(products.map(SearchProductResponse::from));
    }

    @Override
    public FindByIdResponse findById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
            ProductException.ProductNotFoundException::new
        );

        return productMapper.toFindByIdResponse(product);
    }

    @Override
    public SliceDto<FindByPromotionIdResponse> findByPromotionId(
        FindProductByPromotionIdRequestDto dto
    ) {
        Promotion promotion = promotionRepository.findById(dto.promotionId()).orElseThrow(
            ProductException.ProductPromotionIsNotInProgressException::new
        );

        Long companyId = promotion.getCompany().getId();

        Slice<Product> products = productRepository.findByCompanyId(companyId, dto.pageable());
        Slice<FindByPromotionIdResponse> responses = products.map(
            productMapper::toFindByPromotionIdResponse
        );

        return SliceDto.from(responses);
    }

    // TODO : 상품 랭킹 조회 구현 2차
    @Override
    public GetProductRankingResponse getProductRanking() {
        List<ProductRanking> productRankings = null;

        return null;
    }

    @Override
    @Transactional
    public DecreaseStockResponse decreaseStock(
        DecreaseStockRequestDto dto
    ) {
        Product product = findProductByIdOrElseThrow(dto.productId());

        validatePromotionStatus(product);

        ProductOption productOption = findOptionByProductAndIdOrElseThrow(
            product,
            dto.optionId()
        );

        productOption.getProductStock().decrease(dto.decreaseStockAmount());

        Product updatedProduct = productRepository.save(product);

        ProductOption updatedProductOption = findOptionByProductAndIdOrElseThrow(
            updatedProduct,
            dto.optionId()
        );

        return productMapper.toDecreaseStockResponse(
            updatedProduct,
            updatedProductOption,
            dto.decreaseStockAmount()
        );
    }

    @Override
    @Transactional
    public IncreaseStockResponse increaseStock(
        IncreaseStockRequestDto dto
    ) {
        Product product = findProductByIdOrElseThrow(dto.productId());

        validatePromotionStatus(product);

        ProductOption productOption = findOptionByProductAndIdOrElseThrow(
            product,
            dto.optionId()
        );

        productOption.getProductStock().increase(dto.increaseStockAmount());

        Product updatedProduct = productRepository.save(product);

        ProductOption updatedProductOption = findOptionByProductAndIdOrElseThrow(
            updatedProduct,
            dto.optionId()
        );

        return productMapper.toIncreaseStockResponse(
            updatedProduct,
            updatedProductOption,
            dto.increaseStockAmount()
        );
    }

    @Override
    public GetProductDetailsResponse getProductDetails(GetProductDetailsRequestDto dto) {
        List<Long> productIds = dto.productDetails()
            .stream()
            .map(GetProductDetailsRequestDto.ProductDetail::productId)
            .distinct()
            .toList();

        List<Long> optionIds = dto.productDetails()
            .stream()
            .map(GetProductDetailsRequestDto.ProductDetail::optionId)
            .distinct()
            .toList();

        List<Product> products = productRepository.findAllWithDetailsByIdsAndOptionIds(productIds,
            optionIds);

        Map<Long, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<GetProductDetailsResponse.ProductDetail> productDetails = dto.productDetails()
            .stream()
            .map(pd -> {
                Product product = productMap.get(pd.productId());
                if (product == null) {
                    throw new ProductException.ProductNotFoundException();
                }
                ProductOption productOption = product.getOptions().stream()
                    .filter(option -> option.getId().equals(pd.optionId()))
                    .findFirst()
                    .orElseThrow(ProductException.ProductOptionNotFoundException::new);

                return GetProductDetailsResponse.ProductDetail.builder()
                    .promotionStatus(product.getCompany().getPromotion().getStatus())
                    .companyId(product.getCompany().getId())
                    .companyName(product.getCompany().getName().getValue())
                    .productId(product.getId())
                    .productName(product.getName().getValue())
                    .productPreviewImgUrl(product.getPreviewUrl().getValue())
                    .originalPrice(product.getOriginalPrice().getValue())
                    .promotionPrice(product.getPromotionPrice().getValue())
                    .optionId(productOption.getId())
                    .optionName(productOption.getOptionName().getValue())
                    .extraPrice(productOption.getExtraPrice().getValue())
                    .build();
            })
            .toList();

        return GetProductDetailsResponse.builder()
            .productDetails(productDetails)
            .build();
    }

    @Override
    public ValidateStockDecreasesResponse validateStockDecrease(
        ValidateStockDecreasesRequestDto dto
    ) {
        List<Long> companyIds = dto.productDetails()
            .stream()
            .map(ValidateStockDecreasesRequestDto.ProductDetail::companyId)
            .distinct()
            .toList();

        List<Long> optionIds = dto.productDetails()
            .stream()
            .map(ValidateStockDecreasesRequestDto.ProductDetail::optionId)
            .distinct()
            .toList();

        List<Product> products = productRepository.findAllWithDetailsByCompanyIdsAndOptionIds(
            companyIds, optionIds);

        Map<Long, ProductAndOption> productAndOptionMap = new HashMap<>();
        for (Product product : products) {
            for (ProductOption option : product.getOptions()) {
                productAndOptionMap.put(option.getId(), new ProductAndOption(product, option));
            }
        }

        Map<Company, List<ValidateStockDecreasesResponse.CompanyDetail.CompanyProduct>> groupedByCompany =
            dto.productDetails()
                .stream()
                .map(pd -> {
                    ProductAndOption pair = productAndOptionMap.get(pd.optionId());
                    if (pair == null) {
                        throw new ProductException.ProductOptionNotFoundException();
                    }
                    // 검증 1: 요청의 회사 ID와 조회된 Product의 회사 ID가 일치하는지 검증
                    if (!pair.product.getCompany().getId().equals(pd.companyId())) {
                        throw new ProductException.ProductCompanyMismatchException();
                    }

                    // 검증 2: 프로모션 진행 여부
                    PromotionStatus promotionStatus = pair.product.getCompany().getPromotion()
                        .getStatus();
                    if (promotionStatus.equals(PromotionStatus.IN_PROGRESS)) {
                        throw new ProductException.ProductPromotionIsNotInProgressException();
                    }

                    // 검증 3: 재고 확인 (요청한 감소 수량보다 옵션의 재고가 충분한지)
                    Long availableStock = pair.option.getProductStock().getValue();
                    if (availableStock < pd.decreaseStockAmount()) {
                        throw new ProductException.ProductDoNotExistException();
                    }

                    ValidateStockDecreasesResponse.CompanyDetail.CompanyProduct companyProduct =
                        ValidateStockDecreasesResponse.CompanyDetail.CompanyProduct.builder()
                            .promotionStatus(promotionStatus)
                            .productId(pair.product.getId())
                            .productName(pair.product.getName().getValue())
                            .productPreviewImgUrl(pair.product.getPreviewUrl().getValue())
                            .originalPrice(pair.product.getOriginalPrice().getValue())
                            .promotionPrice(pair.product.getPromotionPrice().getValue())
                            .optionId(pair.option.getId())
                            .optionName(pair.option.getOptionName().getValue())
                            .extraPrice(pair.option.getExtraPrice().getValue())
                            .decreaseStockAmount(pd.decreaseStockAmount())
                            .build();

                    return new AbstractMap.SimpleEntry<>(pair.product.getCompany(), companyProduct);
                })
                .collect(Collectors.groupingBy(
                    SimpleEntry::getKey,
                    Collectors.mapping(SimpleEntry::getValue, Collectors.toList())
                ));

        List<ValidateStockDecreasesResponse.CompanyDetail> companyList = groupedByCompany.entrySet()
            .stream()
            .map(entry -> {
                Company company = entry.getKey();
                List<ValidateStockDecreasesResponse.CompanyDetail.CompanyProduct> cpList = entry.getValue();
                return ValidateStockDecreasesResponse.CompanyDetail.builder()
                    .companyId(company.getId())
                    .companyName(company.getName().getValue())
                    .companyProductList(cpList)
                    .build();
            })
            .collect(Collectors.toList());

        return ValidateStockDecreasesResponse.builder()
            .companyList(companyList)
            .build();
    }

    @Override
    public GetProductOptionsResponse getProductOptions(
        GetProductOptionsRequestDto dto
    ) {
        Product product = findProductByIdOrElseThrow(dto.productId());

        List<GetProductOptionsResponse.ProductOption> options = product.getOptions().stream().map(
            option -> GetProductOptionsResponse.ProductOption.builder()
                .optionId(option.getId())
                .optionName(option.getOptionName().getValue())
                .extraPrice(option.getExtraPrice().getValue())
                .productStock(option.getProductStock().getValue())
                .build()
        ).toList();

        return GetProductOptionsResponse.builder()
            .options(options)
            .build();
    }

    public GetProductDetailsResponse getProductDetailsNPlus1(
        GetProductDetailsRequestDto dto
    ) {
        List<GetProductDetailsResponse.ProductDetail> productDetails = dto.productDetails()
            .stream()
            .map(productDetail -> {
                Product product = findProductByIdOrElseThrow(productDetail.productId());
                ProductOption productOption = findOptionByProductAndIdOrElseThrow(
                    product,
                    productDetail.optionId()
                );

                return GetProductDetailsResponse.ProductDetail.builder()
                    .promotionStatus(product.getCompany().getPromotion().getStatus())
                    .companyId(product.getCompany().getId())
                    .companyName(product.getCompany().getName().getValue())
                    .productId(product.getId())
                    .productName(product.getName().getValue())
                    .productPreviewImgUrl(product.getPreviewUrl().getValue())
                    .originalPrice(product.getOriginalPrice().getValue())
                    .promotionPrice(product.getPromotionPrice().getValue())
                    .optionId(productOption.getId())
                    .optionName(productOption.getOptionName().getValue())
                    .extraPrice(productOption.getExtraPrice().getValue())
                    .build();
            })
            .toList();

        return GetProductDetailsResponse.builder()
            .productDetails(productDetails)
            .build();
    }

    private void validatePromotionStatus(Product product) {
        PromotionStatus status = product.getCompany().getPromotion().getStatus();

        if (status == PromotionStatus.FINISHED) {
            throw new PromotionException.PromotionNotInProgressException();
        }
    }

    private Product findProductByIdOrElseThrow(Long productId) {
        return productRepository.findById(productId).orElseThrow(
            ProductException.ProductNotFoundException::new
        );
    }

    private ProductOption findOptionByProductAndIdOrElseThrow(
        Product product,
        Long optionId
    ) {
        return product.getOptions().stream()
            .filter(option -> option.getId().equals(optionId))
            .findFirst()
            .orElseThrow(ProductException.ProductOptionNotFoundException::new
            );
    }

    private record ProductAndOption(
        Product product,
        ProductOption option
    ) {

    }
}
