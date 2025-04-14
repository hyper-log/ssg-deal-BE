package on.ssgdeal.promotion_service.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.promotion_service.domain.vo.validator.UrlValidator;


@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class PromotionContentImageUrl {

    private static final int MAX_LENGTH = 2048;
    private static final String ERROR_MESSAGE_FIELD = "프로모션 내용 이미지 URL";

    @Column(name = "content_image_url", nullable = false, length = MAX_LENGTH)
    private String value;

    public PromotionContentImageUrl(final String value) {
        UrlValidator.validate(value, MAX_LENGTH, ERROR_MESSAGE_FIELD);
        this.value = value;
    }

}
