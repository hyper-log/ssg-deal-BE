package on.ssgdeal.cart_service.infrastructure.persistence.repository.dto;

public record AddCartProductDto(
    String key,
    String hashKey,
    Long hashValue
) {

    public static AddCartProductDto from(String key, String hashKey, Long hashValue) {
        return new AddCartProductDto(key, hashKey, hashValue);
    }
}
