package on.ssgdeal.promotion_service.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.promotion_service.domain.vo.validator.DisplayNameValidator;


@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class OptionName {

    private static final int MAX_LENGTH = 100;
    private static final String ERROR_MESSAGE_FIELD = "옵션 내용";

    @Column(name = "option_name", nullable = false, length = MAX_LENGTH)
    private String value;

    public OptionName(final String value) {
        DisplayNameValidator.validate(value, MAX_LENGTH, ERROR_MESSAGE_FIELD);
        this.value = value;
    }

}
