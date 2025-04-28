package on.ssgdeal.promotion_service.domain.entity.dto;

import lombok.Builder;
import on.ssgdeal.promotion_service.domain.entity.Company;

import java.util.List;

@Builder
public record CreateProductDto(

        Company company,
        String productName,
        Long originalPrice,
        Long promotionPrice,
        String previewUrl,
        String contentImgUrl,
        String content,
        List<CreateProductOptionDto> options

) {
}