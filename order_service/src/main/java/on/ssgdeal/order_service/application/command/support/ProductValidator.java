package on.ssgdeal.order_service.application.command.support;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.order_service.application.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.dto.TotalOrderProductInfo;
import on.ssgdeal.order_service.application.dto.TotalOrderProductInfo.ProductInfo;
import on.ssgdeal.order_service.application.service.PromotionService;
import on.ssgdeal.order_service.exception.OrderException.OrderPromotionStockOver;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoRequestDto.GetProductDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final PromotionService promotionService;

    public GetProductInfoDto valid(CreateOrderRequestDto request) {
        TotalOrderProductInfo totalOrderProductInfo = convertPromotionRequestProductInfo(request);
        var promotionRequestInfo = fromTotalOrderProductInfo(totalOrderProductInfo);

        GetProductInfoDto productInfoDto;
        try {
            log.info("Order > Promotion");
            log.info("주문 생성 상품 정보 조회 요청 : {}", promotionRequestInfo);
            productInfoDto = promotionService.getProductInfoAndStockDecrease(promotionRequestInfo);
            log.info("상품 정보 조회 성공 : {}", productInfoDto);
        } catch (Exception e) {
            log.info("조회 실패 및 재고 감소 불가능 상황 : {}", e.getMessage());
            throw new OrderPromotionStockOver();
        }
        return productInfoDto;
    }

    private TotalOrderProductInfo convertPromotionRequestProductInfo(
        CreateOrderRequestDto request
    ) {
        return new TotalOrderProductInfo(request.subOrders().stream().flatMap(
            sub -> sub.orderedProducts().stream()
                .map(p -> new ProductInfo(p.productId(), p.optionId(), p.quantity()))).toList());
    }

    private GetProductInfoRequestDto fromTotalOrderProductInfo(
        TotalOrderProductInfo totalOrderProductInfo) {
        List<GetProductDetails> details = totalOrderProductInfo.products().stream().map(
            p -> new GetProductInfoRequestDto.GetProductDetails(p.productId(), p.optionId(),
                p.quantity())).toList();

        return new GetProductInfoRequestDto(details);
    }

}
