package on.ssgdeal.promotion_service.presentation.internal;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.promotion_service.application.service.ProductService;
import on.ssgdeal.promotion_service.application.service.dto.product.DecreaseStockRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.GetProductDetailsRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.GetProductOptionsRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.IncreaseStockRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.ValidateStockDecreasesRequestDto;
import on.ssgdeal.promotion_service.presentation.internal.dto.mapper.ProductPresentationMapper;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.DecreaseStockRequest;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.DecreaseStockResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.GetProductDetailsRequest;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.GetProductDetailsResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.GetProductOptionsRequest;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.GetProductOptionsResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.IncreaseStockRequest;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.IncreaseStockResponse;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.ValidateStockDecreasesRequest;
import on.ssgdeal.promotion_service.presentation.internal.dto.product.ValidateStockDecreasesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1/promotions/products")
public class ProductInternalController {

    private final ProductService productService;
    private final ProductPresentationMapper mapper;

    @PostMapping("/stocks/decrease")
    public ResponseEntity<CommonResponse<DecreaseStockResponse>> decreaseStock(
        @RequestBody DecreaseStockRequest request
    ) {
        DecreaseStockRequestDto dto = mapper.toDto(request);
        DecreaseStockResponse response = productService.decreaseStock(dto);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PostMapping("/stocks/increase")
    public ResponseEntity<CommonResponse<IncreaseStockResponse>> increaseStock(
        @RequestBody IncreaseStockRequest request
    ) {
        IncreaseStockRequestDto dto = mapper.toDto(request);
        IncreaseStockResponse response = productService.increaseStock(dto);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PostMapping("/detail")
    public ResponseEntity<CommonResponse<GetProductDetailsResponse>> getProductDetail(
        @RequestBody GetProductDetailsRequest request
    ) {
        GetProductDetailsRequestDto dto = mapper.toDto(request);
        GetProductDetailsResponse response = productService.getProductDetails(dto);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PostMapping("/stocks/decrease/detail")
    public ResponseEntity<CommonResponse<ValidateStockDecreasesResponse>> validateStockDecrease(
        @RequestBody ValidateStockDecreasesRequest request
    ) {
        ValidateStockDecreasesRequestDto dto = mapper.toDto(request);
        ValidateStockDecreasesResponse response = productService.validateStockDecrease(dto);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PostMapping("/options")
    public ResponseEntity<CommonResponse<GetProductOptionsResponse>> getProductOptions(
        @RequestBody GetProductOptionsRequest request
    ) {
        GetProductOptionsRequestDto dto = mapper.toDto(request);
        GetProductOptionsResponse response = productService.getProductOptions(dto);

        return ResponseEntity.ok(CommonResponse.success(response));

    }
}
