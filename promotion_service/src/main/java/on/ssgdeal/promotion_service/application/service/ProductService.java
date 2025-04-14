package on.ssgdeal.promotion_service.application.service;

import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.common.application.dto.SliceDto;
import on.ssgdeal.promotion_service.application.service.dto.product.DecreaseStockRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.FindProductByPromotionIdRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.GetProductDetailsRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.GetProductOptionsRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.IncreaseStockRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.ValidateStockDecreasesRequestDto;
import on.ssgdeal.promotion_service.presentation.external.dto.product.FindByIdResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.FindByPromotionIdResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.GetProductRankingResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.SearchProductResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.DecreaseStockResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.GetProductDetailsResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.GetProductOptionsResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.IncreaseStockResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.ValidateStockDecreasesResponse;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    PageDto<SearchProductResponse> searchWithProductName(String productName, Pageable pageable);

    FindByIdResponse findById(Long productId);

    SliceDto<FindByPromotionIdResponse> findByPromotionId(FindProductByPromotionIdRequestDto dto);

    GetProductRankingResponse getProductRanking();

    DecreaseStockResponse decreaseStock(DecreaseStockRequestDto dto);

    IncreaseStockResponse increaseStock(IncreaseStockRequestDto dto);

    GetProductDetailsResponse getProductDetails(GetProductDetailsRequestDto dto);

    ValidateStockDecreasesResponse validateStockDecrease(ValidateStockDecreasesRequestDto dto);

    GetProductOptionsResponse getProductOptions(GetProductOptionsRequestDto dto);
}
