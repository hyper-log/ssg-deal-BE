package on.ssgdeal.auth_service.domain.repository;

import java.util.Optional;
import on.ssgdeal.auth_service.domain.entity.Auth;
import on.ssgdeal.auth_service.domain.vo.Username;

public interface AuthRepository {

    Auth save(Auth auth);

    Optional<Auth> findByUserId(Long userId);

    Optional<Auth> findByUsername(Username username);

    boolean existsByUsername(Username username);

    void delete(Auth auth);
}
