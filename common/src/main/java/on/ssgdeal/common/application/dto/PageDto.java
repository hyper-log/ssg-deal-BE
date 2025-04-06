package on.ssgdeal.common.application.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageDto<T>(
    List<T> content,
    boolean last,
    int totalPages,
    long totalElements
) {

    public static <T> PageDto<T> from(Page<T> page) {
        return new PageDto<>(
            page.getContent(),
            page.isLast(),
            page.getTotalPages(),
            page.getTotalElements()
        );
    }
}