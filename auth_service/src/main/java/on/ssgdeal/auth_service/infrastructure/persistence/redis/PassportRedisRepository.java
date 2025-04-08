package on.ssgdeal.auth_service.infrastructure.persistence.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j(topic = "AuthRedisRepository")
public class PassportRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    @Value("${passport.expiration.generation}")
    private long passportExpirationTime;

    public String save(String id, String passport) {
        redisTemplate.opsForValue().set(
            id,
            passport,
            passportExpirationTime,
            TimeUnit.MILLISECONDS
        );

        return id;
    }

    public Optional<String> findById(String id) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(id));
    }

    public void delete(String id) {
        redisTemplate.delete(id);
    }

}
