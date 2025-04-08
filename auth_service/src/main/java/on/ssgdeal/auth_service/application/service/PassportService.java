package on.ssgdeal.auth_service.application.service;

import on.ssgdeal.auth_service.domain.entity.Passport;

public interface PassportService {

    Passport getPassportByPassportId(String passportId);

    String createAndStorePassport(String token, Long userId);

    String getPassportIdByRefreshToken(String token);

    Passport getPassportByRefreshToken(String token);

    void expirePassportByRefreshToken(String refreshToken);

    void deletePassportByRefreshToken(String refreshToken);

    String createAndStoreNewPassportByRefreshToken(String refreshToken, String newRefreshToken);

    void deletePassportByPassportId(String passportId);
}
