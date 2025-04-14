package on.ssgdeal.promotion_service.application.service.dto;

import lombok.Builder;
import on.ssgdeal.common.application.dto.SliceDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetInProgressPromotionDetailDto;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Builder
public record GetInProgressPromotionDetailResponseDto(
        Long promotionId,
        String promotionTitle,
        String contentImageUrl,
        String promotionContent,
        String companyName,
        String companyLogoUrl,
        LocalDate startPromotionDate,
        LocalDate endPromotionDate,
        SliceDto<ProductDetailDto> promotionProducts

) {
    public record ProductDetailDto(
            Long productId,
            String productName,
            String previewUrl,
            Long originalPrice,
            Long promotionPrice
    ) {
        public static ProductDetailDto from(GetInProgressPromotionDetailDto.ProductDetailDto dto) {
            return new ProductDetailDto(
                    dto.productId(),
                    dto.productName(),
                    dto.previewUrl(),
                    dto.originalPrice(),
                    dto.promotionPrice()
            );
        }
    }
    public static GetInProgressPromotionDetailResponseDto from(GetInProgressPromotionDetailDto dto) {

        Slice<GetInProgressPromotionDetailResponseDto.ProductDetailDto> productsToResponseDto =
                new SliceImpl<>(
                        dto.promotionProducts().getContent().stream()
                                .map(GetInProgressPromotionDetailResponseDto.ProductDetailDto::from)
                                .collect(Collectors.toList()),
                        dto.promotionProducts().getPageable(),
                        dto.promotionProducts().hasNext()
                );

        return GetInProgressPromotionDetailResponseDto.builder()
                .promotionId(dto.promotionId())
                .promotionTitle(dto.promotionTitle())
                .contentImageUrl(dto.contentImageUrl())
                .promotionContent(dto.promotionContent())
                .companyName(dto.companyName())
                .companyLogoUrl(dto.companyLogoUrl())
                .startPromotionDate(dto.startPromotionDate())
                .endPromotionDate(dto.endPromotionDate())
                .promotionProducts(SliceDto.from(productsToResponseDto))
                .build();
    }
}
