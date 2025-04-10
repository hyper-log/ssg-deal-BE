package on.ssgdeal.common.auth.passport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.auth.passport.exception.PassportException.PassportRetrievalFailedException;
import on.ssgdeal.common.auth.passport.exception.PassportException.PassportValidationFailedException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "PassportUtil")
public class PassportUtil {

    public static final String PASSPORT_ID_HEADER = "X-Passport-Id";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public Passport getPassportBy(String passportId) {
        String json = redisTemplate.opsForValue().get(passportId);

        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, Passport.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new PassportRetrievalFailedException();
        }
    }

    public Passport getPassportBy(HttpServletRequest httpServletRequest) {
        String passportId = httpServletRequest.getHeader(PASSPORT_ID_HEADER);

        if (passportId == null) {
            throw new PassportValidationFailedException();
        }

        return getPassportBy(passportId);
    }

    public void hasRole(Passport passport, String role) {
        if (!passport.getRole().equals(role)) {
            throw new PassportValidationFailedException();
        }
    }
}
