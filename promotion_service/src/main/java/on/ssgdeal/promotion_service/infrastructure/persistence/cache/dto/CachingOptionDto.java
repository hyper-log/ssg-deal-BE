package on.ssgdeal.promotion_service.infrastructure.persistence.cache.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CachingOptionDto {

    private Long optionId;
    private String optionName;
    private Long extraPrice;
    private Long productStock;
}