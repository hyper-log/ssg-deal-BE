package on.ssgdeal.common.pageable;


import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.pageable.enums.PageNumber;
import on.ssgdeal.common.pageable.enums.PageSize;
import on.ssgdeal.common.pageable.enums.PageSortBy;
import on.ssgdeal.common.pageable.exception.PageableException.InvalidPageNumberException;
import on.ssgdeal.common.pageable.exception.PageableException.InvalidPageSizeException;
import on.ssgdeal.common.pageable.exception.PageableException.InvalidSortByException;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
public class PageableVerificationArgumentResolver extends PageableHandlerMethodArgumentResolver {

    private final SortArgumentResolver resolver = new SortHandlerMethodArgumentResolver();

    @Override
    public Pageable resolveArgument(
        MethodParameter methodParameter,
        @Nullable ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        @Nullable WebDataBinderFactory binderFactory
    ) {

        String pageText = webRequest.getParameter(
            getParameterNameToUse(getPageParameterName(), methodParameter));
        String sizeText = webRequest.getParameter(
            getParameterNameToUse(getSizeParameterName(), methodParameter));
        Sort sort = resolver.resolveArgument(methodParameter, mavContainer, webRequest,
            binderFactory);

        log.info("pageText: {}", pageText);
        log.info("sizeText: {}", sizeText);
        log.info("sort: {}", sort);

        if (pageText == null) {
            pageText = String.valueOf(PageNumber.MINIMUM_PAGE_NUMBER.getNumber());
        }
        if (sizeText == null) {
            sizeText = String.valueOf(PageSize.DEFAULT.getSize());
        }
        if (!sort.isSorted()) {
            sort = Sort.by(
                Sort.Direction.DESC,
                PageSortBy.CREATED_AT.getSortBy(),
                PageSortBy.UPDATED_AT.getSortBy(),
                PageSortBy.ID.getSortBy()
            );
        }

        validatePage(pageText);
        validatePageSize(sizeText);
        validateSort(sort);

        return PageRequest.of(Integer.parseInt(pageText), Integer.parseInt(sizeText), sort);
    }

    private void validatePage(String pageText) {
        if (StringUtils.hasText(pageText)) {
            try {
                int page = Integer.parseInt(pageText);
                if (!PageNumber.isValid(page)) {
                    throw new InvalidPageNumberException();
                }
            } catch (NumberFormatException e) {
                throw new InvalidPageNumberException();
            }
        }
    }

    private void validatePageSize(String sizeText) {
        if (sizeText != null && !sizeText.isEmpty()) {
            try {
                int size = Integer.parseInt(sizeText);
                if (!PageSize.isValid(size)) {
                    throw new InvalidPageSizeException();
                }
            } catch (NumberFormatException e) {
                throw new InvalidPageSizeException();
            }
        }
    }

    private void validateSort(Sort sort) {
        if (sort != null) {
            sort.forEach(order -> {
                if (!PageSortBy.isValid(order.getProperty())) {
                    throw new InvalidSortByException();
                }
            });
        }
    }
}
