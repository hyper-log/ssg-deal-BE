package on.ssgdeal.auth_service.infrastructure.persistence.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.auth_service.domain.entity.Auth;
import on.ssgdeal.auth_service.domain.repository.AuthRepository;
import on.ssgdeal.auth_service.domain.vo.Username;
import on.ssgdeal.auth_service.infrastructure.persistence.jpa.AuthJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepository {

    private final AuthJpaRepository authJpaRepository;

    @Override
    public Auth save(Auth auth) {
        return authJpaRepository.save(auth);
    }

    @Override
    public Optional<Auth> findByUserId(Long userId) {
        return authJpaRepository.findByUserId(userId);
    }

    @Override
    public Optional<Auth> findByUsername(Username username) {
        return authJpaRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(Username username) {
        return authJpaRepository.existsByUsername(username);
    }

    @Override
    public void delete(Auth auth) {
        authJpaRepository.delete(auth);
    }
}
