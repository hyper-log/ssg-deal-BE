package on.ssgdeal.auth_service.infrastructure.persistence.jpa.querydsl;

import java.util.Optional;
import on.ssgdeal.auth_service.domain.entity.Auth;

public interface AuthQueryDslRepository {

    Optional<Auth> findById(Long id);
}
