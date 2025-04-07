package on.ssgdeal.auth_service.application.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.auth_service.infrastructure.client.user_service.UserClientService;
import on.ssgdeal.auth_service.infrastructure.client.user_service.feign.dto.UserFindByIdResponse;
import on.ssgdeal.auth_service.infrastructure.security.jwt.JwtUtil;
import on.ssgdeal.auth_service.infrastructure.security.passport.Passport;
import on.ssgdeal.auth_service.infrastructure.security.passport.PassportUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PassportServiceImpl")
public class PassportServiceImpl implements PassportService {

    private final PassportUtil passportUtil;
    private final JwtUtil jwtUtil;
    private final UserClientService userClientService;
    @Value("${passport.expiration.generation}")
    private long passportExpirationTime;
    @Value("${passport.expiration.expected}")
    private long passportExpirationExpected;

    @Override
    public Passport getPassportByPassportId(String passportId) {
        return passportUtil.getPassportById(passportId);
    }

    public String createAndStorePassport(String token, Long userId) {
        UserFindByIdResponse dto = userClientService.findUserById(userId);
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        String username = claims.getSubject();
        String role = claims.get("role", String.class);
        Passport passport = Passport.from(dto, username, role);

        return passportUtil.createPassport(token, passport, passportExpirationTime);
    }

    @Override
    public String getPassportIdByRefreshToken(String token) {
        return passportUtil.getPassportKeyByRefreshToken(token);
    }

    @Override
    public Passport getPassportByRefreshToken(String token) {
        return passportUtil.getPassportByRefreshToken(token);
    }

    @Override
    public void expirePassportByRefreshToken(String refreshToken) {
        passportUtil.expirePassportByRefreshToken(refreshToken, passportExpirationExpected);
    }

    @Override
    public void deletePassportByRefreshToken(String refreshToken) {
        passportUtil.deletePassport(refreshToken);
    }

    @Override
    public String createAndStoreNewPassportByRefreshToken(String refreshToken,
        String newRefreshToken) {
        Passport passport = getPassportByRefreshToken(refreshToken);
        return passportUtil.createPassport(newRefreshToken, passport, passportExpirationTime);
    }

    @Override
    public void deletePassportByPassportId(String passportId) {
        passportUtil.deletePassportByPassportId(passportId);
    }

}
