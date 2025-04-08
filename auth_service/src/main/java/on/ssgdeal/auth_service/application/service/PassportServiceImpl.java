package on.ssgdeal.auth_service.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.auth_service.domain.entity.Passport;
import on.ssgdeal.auth_service.domain.repository.PassportRepository;
import on.ssgdeal.auth_service.infrastructure.client.user_service.UserClientService;
import on.ssgdeal.auth_service.infrastructure.client.user_service.feign.dto.UserFindByIdResponse;
import on.ssgdeal.auth_service.infrastructure.security.hash.HashUtil;
import on.ssgdeal.auth_service.infrastructure.security.jwt.JwtUtil;
import on.ssgdeal.common.auth.passport.exception.PassportException;
import on.ssgdeal.common.auth.passport.exception.PassportException.PassportRetrievalFailedException;
import on.ssgdeal.common.auth.passport.exception.PassportExceptionCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PassportServiceImpl")
public class PassportServiceImpl implements PassportService {

    private static final String PASSPORT_KEY_PREFIX = "passport:";
    private final JwtUtil jwtUtil;
    private final UserClientService userClientService;
    private final ObjectMapper objectMapper;
    private final HashUtil hashUtil;
    private final PassportRepository passportRepository;


    @Override
    public Passport getPassportByPassportId(String passportId) {
        String json = passportRepository.findById(passportId).orElseThrow(
            PassportRetrievalFailedException::new
        );

        try {
            return objectMapper.readValue(json, Passport.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new PassportException(PassportExceptionCode.PASSPORT_RETRIEVAL_FAILED);
        }
    }

    public String createAndStorePassport(String token, Long userId) {
        UserFindByIdResponse dto = userClientService.findUserById(userId);
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        String username = claims.getSubject();
        String role = claims.get("role", String.class);
        Passport passport = Passport.from(dto, username, role);

        return savePassportToRedis(token, passport);
    }

    @Override
    public String getPassportIdByRefreshToken(String token) {
        return getPassportKey(token);
    }

    @Override
    public Passport getPassportByRefreshToken(String token) {
        String json = passportRepository.findById(getPassportKey(token)).orElseThrow(
            PassportRetrievalFailedException::new
        );

        try {
            return objectMapper.readValue(json, Passport.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new PassportException(PassportExceptionCode.PASSPORT_CREATION_FAILED);
        }
    }

    @Override
    public void expirePassportByRefreshToken(String refreshToken) {
        Passport passport = getPassportByRefreshToken(refreshToken);

        if (passport == null) {
            throw new PassportException(PassportExceptionCode.PASSPORT_RETRIEVAL_FAILED);
        }

        passportRepository.delete(getPassportKey(refreshToken));
    }

    @Override
    public void deletePassportByRefreshToken(String refreshToken) {
        passportRepository.delete(getPassportKey(refreshToken));
    }

    @Override
    public String createAndStoreNewPassportByRefreshToken(String refreshToken,
        String newRefreshToken) {
        Passport passport = getPassportByRefreshToken(refreshToken);
        return savePassportToRedis(newRefreshToken, passport);
    }

    @Override
    public void deletePassportByPassportId(String passportId) {
        passportRepository.delete(passportId);
    }

    public String savePassportToRedis(String token, Passport passport) {
        try {
            String passportJson = objectMapper.writeValueAsString(passport);
            String passportId = getPassportKey(token);

            return passportRepository.save(passportId, passportJson);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new PassportException(PassportExceptionCode.PASSPORT_CREATION_FAILED);
        }
    }

    private String getPassportKey(String token) {
        String key = hashUtil.sha256Hex(token);
        return PASSPORT_KEY_PREFIX + key;
    }

}
