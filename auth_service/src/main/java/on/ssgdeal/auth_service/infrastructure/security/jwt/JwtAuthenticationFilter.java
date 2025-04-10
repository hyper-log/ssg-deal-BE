package on.ssgdeal.auth_service.infrastructure.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.auth_service.application.service.PassportService;
import on.ssgdeal.auth_service.exception.AuthException;
import on.ssgdeal.auth_service.exception.AuthExceptionCode;
import on.ssgdeal.auth_service.infrastructure.security.cookie.CookieUtil;
import on.ssgdeal.auth_service.infrastructure.security.details.AuthDetailsImpl;
import on.ssgdeal.auth_service.presentation.external.dto.LoginAuthRequest;
import on.ssgdeal.auth_service.presentation.external.dto.LoginAuthResponse;
import on.ssgdeal.common.auth.enums.AuthRole;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "JwtAuthenticationFilter")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final PassportService passportService;

    @Builder
    public JwtAuthenticationFilter(
        AuthenticationManager authenticationManager,
        JwtUtil jwtUtil,
        CookieUtil cookieUtil,
        PassportService passportService
    ) {
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/api/v1/auth/login");
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.passportService = passportService;
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        try {
            log.info("attemptAuthentication, {}", request.getRequestURI());

            LoginAuthRequest loginRequest = new ObjectMapper()
                .readValue(
                    request.getInputStream(),
                    LoginAuthRequest.class
                );

            return getAuthenticationManager()
                .authenticate(
                    new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password(),
                        null
                    )
                );
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AuthenticationException e) {
            throw new AuthException(AuthExceptionCode.AUTH_IS_NOT_FOUND);
        }
    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication auth
    ) throws IOException {
        log.info("successfulAuthentication, {}", auth.getName());

        AuthDetailsImpl authDetails = (AuthDetailsImpl) auth.getPrincipal();
        String username = authDetails.getUsername();
        AuthRole authRole = authDetails.getRole();

        String accessToken = jwtUtil.generateAccessToken(username, authRole);
        String refreshToken = jwtUtil.generateRefreshToken(username, authRole);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
        cookieUtil.addRefreshTokenToCookie(response, jwtUtil.getTokenWithoutBearer(refreshToken));

        passportService.createAndStorePassport(jwtUtil.getTokenWithoutBearer(refreshToken),
            authDetails.getUserId());

        LoginAuthResponse loginResponse = new LoginAuthResponse(username);
        CommonResponse<LoginAuthResponse> commonResponse = CommonResponse.success(loginResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");

        new ObjectMapper().writeValue(response.getWriter(), commonResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException failed
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        CommonResponse<?> errorResponse =
            CommonResponse.exception("아이디 또는 비밀번호가 일치하지 않습니다.");

        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }

}
