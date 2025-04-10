package on.ssgdeal.auth_service.infrastructure.persistence.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.auth_service.domain.repository.PassportRepository;
import on.ssgdeal.auth_service.infrastructure.persistence.redis.PassportRedisRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PassportRepositoryImpl implements PassportRepository {

    private final PassportRedisRepository passportRedisRepository;

    @Override
    public String save(String id, String passport) {
        passportRedisRepository.save(id, passport);

        return id;
    }

    @Override
    public Optional<String> findById(String id) {
        return passportRedisRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        passportRedisRepository.delete(id);
    }
}
