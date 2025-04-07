package on.ssgdeal.auth_service.infrastructure.persistence.jpa.querydsl;

import java.util.Optional;
import on.ssgdeal.auth_service.domain.entity.Auth;
import org.springframework.stereotype.Repository;

@Repository
public class AuthQueryDslRepositoryImpl implements AuthQueryDslRepository {


    @Override
    public Optional<Auth> findById(Long id) {
        return Optional.empty();
    }
}
