package on.ssgdeal.auth_service.infrastructure.persistence.jpa;

import java.util.Optional;
import on.ssgdeal.auth_service.domain.entity.Auth;
import on.ssgdeal.auth_service.domain.vo.Username;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthJpaRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findByUsername(Username username);

    Optional<Auth> findByUserId(Long userId);

    boolean existsByUsername(Username username);
}
