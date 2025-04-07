package on.ssgdeal.gateway_service.presentation.dto;

public record CommonResponse<T>(
    String message,
    T data
) {

    private static final String SUCCESS = "SUCCESS";

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(SUCCESS, data);
    }

    public static <T> CommonResponse<T> success() {
        return new CommonResponse<>(SUCCESS, null);
    }

    public static <T> CommonResponse<T> exception(String message) {
        return new CommonResponse<>(message, null);
    }

    public static <T> CommonResponse<T> exception(String message, T data) {
        return new CommonResponse<>(message, data);
    }
}