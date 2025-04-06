package on.ssgdeal.common.pageable.enums;

import lombok.Getter;

@Getter
public enum PageNumber {
    MINIMUM_PAGE_NUMBER(0),
    MAXIMUM_PAGE_NUMBER(1000),
    ;

    private final int number;

    PageNumber(int number) {
        this.number = number;
    }

    public static boolean isValid(int page) {
        return page >= MINIMUM_PAGE_NUMBER.getNumber() && page <= MAXIMUM_PAGE_NUMBER.getNumber();
    }
}
