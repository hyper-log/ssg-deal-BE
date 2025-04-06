package on.ssgdeal.common.global.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.pageable.exception.PageableException;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PageableException.class)
    public ResponseEntity<Object> handlePageableException(PageableException e) {
        log.warn("handlePageableException", e);
        ExceptionCode exceptionCode = e.getException();
        return handleExceptionInternal(exceptionCode);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        log.warn("handleCustomException", e);
        ExceptionCode exceptionCode = e.getException();
        return handleExceptionInternal(exceptionCode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException e,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {
        log.warn("handleValidationExceptions", e);
        ExceptionCode exceptionCode = CommonExceptionCode.ILLEGAL_ARGUMENT;
        Map<String, String> errorData = extractExceptionBindingPoint(e.getBindingResult());
        return handleExceptionInternal(e.getMessage(), errorData, exceptionCode);
    }

    private Map<String, String> extractExceptionBindingPoint(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors()
            .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("handleIllegalArgument", e);
        ExceptionCode exceptionCode = CommonExceptionCode.ILLEGAL_ARGUMENT;
        return handleExceptionInternal(e.getMessage(), exceptionCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception ex) {
        log.warn("handleAllException", ex);
        ExceptionCode exceptionCode = CommonExceptionCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(ex.getMessage(), exceptionCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(
        String message,
        Map<String, String> data,
        ExceptionCode exceptionCode
    ) {
        return ResponseEntity.status(exceptionCode.getHttpStatus())
            .body(makeErrorResponse(message, data));
    }

    private CommonResponse<Object> makeErrorResponse(String message, Map<String, String> data) {
        return CommonResponse.exception(message, data);
    }


    private ResponseEntity<Object> handleExceptionInternal(ExceptionCode exceptionCode) {
        return ResponseEntity.status(exceptionCode.getHttpStatus())
            .body(makeErrorResponse(exceptionCode));
    }

    private CommonResponse<Object> makeErrorResponse(ExceptionCode exceptionCode) {
        return CommonResponse.exception(exceptionCode.getMessage());
    }

    private ResponseEntity<Object> handleExceptionInternal(String message,
        ExceptionCode exceptionCode) {
        return ResponseEntity.status(exceptionCode.getHttpStatus())
            .body(makeErrorResponse(message));
    }

    private CommonResponse<Object> makeErrorResponse(String message) {
        return CommonResponse.exception(message);
    }
}
