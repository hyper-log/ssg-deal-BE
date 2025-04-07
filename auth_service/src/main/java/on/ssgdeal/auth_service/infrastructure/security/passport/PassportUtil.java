package on.ssgdeal.auth_service.infrastructure.security.passport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.auth_service.infrastructure.security.hash.HashUtil;
import on.ssgdeal.common.auth.passport.exception.PassportException;
import on.ssgdeal.common.auth.passport.exception.PassportExceptionCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "PassportUtil")
public class PassportUtil {

    private static final String PASSPORT_KEY_PREFIX = "passport:";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final HashUtil hashUtil;

    private String getPassportKey(String token) {
        String key = hashUtil.sha256Hex(token);
        return PASSPORT_KEY_PREFIX + key;
    }

    public String createPassport(String token, Passport passport, long ttlSeconds) {
        try {
            String json = objectMapper.writeValueAsString(passport);
            String passportId = getPassportKey(token);
            redisTemplate.opsForValue().set(passportId, json, ttlSeconds,
                TimeUnit.SECONDS);
            return passportId;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new PassportException(PassportExceptionCode.PASSPORT_CREATION_FAILED);
        }
    }

    public Passport getPassportByRefreshToken(String token) {
        String json = redisTemplate.opsForValue().get(getPassportKey(token));
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, Passport.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new PassportException(PassportExceptionCode.PASSPORT_CREATION_FAILED);
        }
    }

    public String getPassportKeyByRefreshToken(String token) {
        return getPassportKey(token);
    }

    public Passport getPassportById(String passportId) {
        String json = redisTemplate.opsForValue().get(passportId);
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, Passport.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new PassportException(PassportExceptionCode.PASSPORT_RETRIEVAL_FAILED);
        }
    }

    public void updatePassport(String token, Passport updatedPassport, long ttlSeconds) {
        createPassport(token, updatedPassport, ttlSeconds);
    }

    public void deletePassport(String token) {
        redisTemplate.delete(getPassportKey(token));
    }

    public boolean isAuthenticated(String token) {
        return getPassportByRefreshToken(token) != null;
    }

    public void expirePassportByRefreshToken(String refreshToken, long passportExpireAfterSeconds) {
        Passport passport = getPassportByRefreshToken(refreshToken);

        if (passport == null) {
            throw new PassportException(PassportExceptionCode.PASSPORT_RETRIEVAL_FAILED);
        }

        updatePassport(refreshToken, passport, passportExpireAfterSeconds);
    }

    public void deletePassportByPassportId(String passportId) {
        redisTemplate.delete(passportId);
    }
}
