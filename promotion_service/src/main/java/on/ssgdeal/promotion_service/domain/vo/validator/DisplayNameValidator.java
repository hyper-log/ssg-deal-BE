package on.ssgdeal.promotion_service.domain.vo.validator;

public class DisplayNameValidator {
    public static void validate(String value, int max, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + "은 필수입니다.");
        }
        if (value.length() > max) {
            throw new IllegalArgumentException(field + "은 " + max + "자 이하로 입력해 주세요.");
        }
    }
}
