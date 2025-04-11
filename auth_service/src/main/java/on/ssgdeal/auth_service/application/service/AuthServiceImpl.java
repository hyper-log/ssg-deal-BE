package on.ssgdeal.auth_service.application.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.auth_service.application.service.dto.SignupAuthRequestDto;
import on.ssgdeal.auth_service.application.service.dto.SignupAuthResponseDto;
import on.ssgdeal.auth_service.domain.entity.Auth;
import on.ssgdeal.auth_service.domain.entity.Passport;
import on.ssgdeal.auth_service.domain.repository.AuthRepository;
import on.ssgdeal.auth_service.domain.util.AuditFieldUpdater;
import on.ssgdeal.auth_service.exception.AuthException;
import on.ssgdeal.auth_service.exception.AuthExceptionCode;
import on.ssgdeal.auth_service.infrastructure.client.user_service.UserClientService;
import on.ssgdeal.auth_service.infrastructure.client.user_service.dto.UserCreateRequest;
import on.ssgdeal.auth_service.infrastructure.client.user_service.feign.dto.UserCreateResponse;
import on.ssgdeal.auth_service.infrastructure.security.cookie.CookieUtil;
import on.ssgdeal.auth_service.infrastructure.security.jwt.JwtUtil;
import on.ssgdeal.auth_service.presentation.external.dto.SignupAuthResponse;
import on.ssgdeal.auth_service.presentation.internal.dto.ReissueTokensAuthResponse;
import on.ssgdeal.auth_service.presentation.internal.dto.ValidateAuthResponse;
import on.ssgdeal.common.auth.enums.AuthRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AuthServiceImpl")
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserClientService userClientService;
    private final CookieUtil cookieUtil;
    private final PassportService passportService;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupAuthResponse signup(
        SignupAuthRequestDto authRequestDto
    ) {

        if (authRepository.existsByUsername(authRequestDto.username())) {
            throw new AuthException(AuthExceptionCode.AUTH_USERNAME_DUPLICATE);
        }

        UserCreateRequest userRequest = UserCreateRequest.from(authRequestDto);
        UserCreateResponse userResponse = userClientService.createUser(userRequest);

        String encodedPassword = passwordEncoder.encode(authRequestDto.password().toString());
        Auth auth = Auth.from(authRequestDto, userResponse, encodedPassword);

        AuditFieldUpdater.updateAuditFields(auth, userResponse.userId());

        Auth savedAuth = authRepository.save(auth);

        SignupAuthResponseDto authResponseDto = SignupAuthResponseDto.from(savedAuth, userResponse);

        return SignupAuthResponse.from(authResponseDto);
    }

    @Override
    public ValidateAuthResponse validate(
        HttpServletRequest request
    ) {
        String refreshToken = cookieUtil.getRefreshTokenFromCookie(request);
        String passportId = passportService.getPassportIdByRefreshToken(refreshToken);

        return ValidateAuthResponse.of(passportId);
    }

    @Override
    public void logout(
        HttpServletRequest request
    ) {
        String refreshToken = cookieUtil.getRefreshTokenFromCookie(request);
        passportService.deletePassportByRefreshToken(refreshToken);
    }

    @Override
    public ReissueTokensAuthResponse reIssueTokens(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        String refreshToken = cookieUtil.getRefreshTokenFromCookie(request);

        Claims claims = jwtUtil.getUserInfoFromToken(refreshToken);
        String username = claims.getSubject();
        String roleClaim = claims.get("role", String.class);
        AuthRole role = AuthRole.valueOf(roleClaim);

        String reIssuedRefreshToken = jwtUtil.generateRefreshToken(username, role);
        String reIssuedAccessToken = jwtUtil.generateAccessToken(username, role);
        String passportId = passportService.createAndStoreNewPassportByRefreshToken(
            refreshToken,
            reIssuedRefreshToken
        );

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, reIssuedAccessToken);
        cookieUtil.addRefreshTokenToCookie(response, reIssuedRefreshToken);
        return ReissueTokensAuthResponse.from(passportId);
    }

    @Override
    public void withdrawUserByPassport(
        HttpServletRequest request
    ) {
        String passportId = request.getHeader("X-Passport-Id");
        Passport passport = passportService.getPassportByPassportId(passportId);
        Long userId = passport.getUserId();
        Auth auth = authRepository.findByUserId(userId).orElseThrow(
            () -> new AuthException(AuthExceptionCode.AUTH_IS_NOT_FOUND)
        );

        log.info("withdrawUserByPassport, {}", userId);
        userClientService.withdrawUserByUserId(passport.getUserId());

        log.info("withdrawPassport, {}", userId);
        passportService.deletePassportByPassportId(passportId);

        authRepository.delete(auth);

    }

    @Override
    public void withdrawUserByUserId(
        Long userId,
        HttpServletRequest request
    ) {
        String passportId = request.getHeader("X-Passport-Id");
        Passport passport = passportService.getPassportByPassportId(passportId);
        Auth auth = authRepository.findByUserId(userId).orElseThrow(
            () -> new AuthException(AuthExceptionCode.AUTH_IS_NOT_FOUND)
        );

        log.info("withdrawUserByUserId, {}", userId);
        userClientService.withdrawUserByUserId(passport.getUserId());

        log.info("withdrawPassport, {}", userId);
        passportService.deletePassportByPassportId(passportId);

        authRepository.delete(auth);
    }


}
