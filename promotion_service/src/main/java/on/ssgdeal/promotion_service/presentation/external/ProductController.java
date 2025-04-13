package on.ssgdeal.promotion_service.presentation.external;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.common.application.dto.SliceDto;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.promotion_service.application.service.ProductService;
import on.ssgdeal.promotion_service.application.service.dto.product.FindProductByPromotionIdRequestDto;
import on.ssgdeal.promotion_service.presentation.external.dto.product.FindByIdResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.FindByPromotionIdResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.GetProductRankingResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.SearchProductResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotions")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products/search")
    public ResponseEntity<CommonResponse<PageDto<SearchProductResponse>>> search(
        @PageableDefault Pageable pageable
    ) {
        PageDto<SearchProductResponse> response = productService.search(pageable);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/all/products/{productId}")
    public ResponseEntity<CommonResponse<FindByIdResponse>> findById(
        @PathVariable Long productId
    ) {
        FindByIdResponse response = productService.findById(productId);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/{promotionId}/products")
    public ResponseEntity<CommonResponse<SliceDto<FindByPromotionIdResponse>>> findByPromotionId(
        @PathVariable Long promotionId,
        @PageableDefault Pageable pageable
    ) {
        FindProductByPromotionIdRequestDto dto = FindProductByPromotionIdRequestDto.from(
            promotionId,
            pageable
        );

        SliceDto<FindByPromotionIdResponse> response = productService.findByPromotionId(dto);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/products/rankings")
    public ResponseEntity<CommonResponse<GetProductRankingResponse>> getProductRanking() {
        GetProductRankingResponse response = productService.getProductRanking();

        return ResponseEntity.ok(CommonResponse.success(response));
    }


}
