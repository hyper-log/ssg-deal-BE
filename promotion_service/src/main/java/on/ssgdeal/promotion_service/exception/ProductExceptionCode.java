package on.ssgdeal.promotion_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductExceptionCode implements ExceptionCode {

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    PRODUCT_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "상품의 옵션을 찾을 수 없습니다."),
    PRODUCT_DO_NOT_EXIST(HttpStatus.BAD_REQUEST, "선택한 상품의 재고가 부족합니다."),
    PRODUCT_COMPANY_MISMATCH(HttpStatus.NOT_FOUND, "선택한 업체의 상품이 아닙니다."),
    PRODUCT_PROMOTION_FINISHED(HttpStatus.BAD_REQUEST, "프로모션이 종료된 상품입니다."),
    PRODUCT_PROMOTION_IS_NOT_IN_PROGRESS(HttpStatus.BAD_REQUEST, "진행중인 프로모션의 상품이 아닙니다."),
    PRODUCT_VERSION_CONFLICT(HttpStatus.CONFLICT, "상품/옵션 수정 중 오류가 발생했습니다."),
    PRODUCT_CACHE_SERIALIZE_FAILED(HttpStatus.BAD_REQUEST, "상품 업데이트 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
