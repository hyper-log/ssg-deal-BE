package on.ssgdeal.cart_service.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.cart_service.application.service.dto.DeleteCartProductRequestDto;
import on.ssgdeal.cart_service.domain.repository.CartRepository;
import on.ssgdeal.cart_service.infrastructure.persistence.generator.RedisKeyGenerator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "cart-service")
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public void deleteCartProducts(DeleteCartProductRequestDto requestDto) {
        log.info("deleteCartProducts - requestDto: {}", requestDto);

        String key = RedisKeyGenerator.generateKey(requestDto.userId());
        List<String> hashKeys = requestDto.productOptions().stream()
            .map(productOption -> RedisKeyGenerator.generateHashKey(
                productOption.productId(), productOption.optionId()))
            .toList();

        cartRepository.deleteCartProducts(key, hashKeys);
    }
}
