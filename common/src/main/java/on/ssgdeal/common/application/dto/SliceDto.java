package on.ssgdeal.common.application.dto;

import java.util.List;
import org.springframework.data.domain.Slice;

public record SliceDto<T>(
    List<T> content,
    boolean hasNext
) {

    public static <T> SliceDto<T> from(Slice<T> slice) {
        return new SliceDto<>(
            slice.getContent(),
            slice.hasNext()
        );
    }
}
