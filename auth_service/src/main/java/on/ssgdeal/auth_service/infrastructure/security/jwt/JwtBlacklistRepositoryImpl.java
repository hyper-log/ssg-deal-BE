package on.ssgdeal.auth_service.infrastructure.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JwtBlacklistRepositoryImpl implements JwtBlacklistRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void addBlacklist(String token) {
    }

    @Override
    public void removeBlacklist(String token) {

    }

    @Override
    public boolean isBlacklisted(String token) {
        return false;
    }
}
