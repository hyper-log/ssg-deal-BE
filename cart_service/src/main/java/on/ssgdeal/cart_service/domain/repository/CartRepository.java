package on.ssgdeal.cart_service.domain.repository;

import java.util.List;

public interface CartRepository {

    void deleteCartProducts(String key, List<String> hashKeys);
}
