package on.ssgdeal.promotion_service.domain.vo.validator;

public class PriceValidator {
    public static void validate(Long value, int min, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + "은 필수입니다.");
        }
        if (value < min) {
            throw new IllegalArgumentException(field + "은 " + min + " 이상으로 입력해 주세요.");
        }
    }
}
