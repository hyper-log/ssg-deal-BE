package on.ssgdeal.auth_service.domain.repository;

import java.util.Optional;

public interface PassportRepository {

    String save(String id, String passport);

    Optional<String> findById(String id);

    void delete(String id);

}
