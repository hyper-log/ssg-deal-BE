package on.ssgdeal.promotion_service.domain.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PromotionStatus {

    PENDING("예정"),
    IN_PROGRESS("진행 중"),
    FINISHED("종료"),
    ;
    private final String description;

}
