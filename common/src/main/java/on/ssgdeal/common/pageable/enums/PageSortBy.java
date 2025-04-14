package on.ssgdeal.common.pageable.enums;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PageSortBy {
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    ID("id");

    private final String sortBy;

    PageSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public static boolean isValid(String sortBy) {
        return Arrays.stream(values())
            .anyMatch(e -> e.getSortBy().equals(sortBy));
    }
}
