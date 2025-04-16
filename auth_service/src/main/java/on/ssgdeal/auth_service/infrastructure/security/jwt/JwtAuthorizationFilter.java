package on.ssgdeal.auth_service.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.auth_service.application.service.PassportService;
import on.ssgdeal.auth_service.exception.JwtException;
import on.ssgdeal.auth_service.exception.JwtExceptionCode;
import on.ssgdeal.auth_service.infrastructure.security.cookie.CookieUtil;
import on.ssgdeal.auth_service.infrastructure.security.details.AuthDetails;
import on.ssgdeal.auth_service.infrastructure.security.details.AuthDetailsService;
import on.ssgdeal.common.auth.enums.AuthRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JwtAuthorizationFilter")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final AuthDetailsService authDetailsService;
    private final PassportService passportService;

    @Builder
    public JwtAuthorizationFilter(
        JwtUtil jwtUtil,
        CookieUtil cookieUtil,
        AuthDetailsService authDetailsService,
        PassportService passportService) {

        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.authDetailsService = authDetailsService;
        this.passportService = passportService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();
        String ipAddress = request.getRemoteAddr();
        log.info("request path and method: {}, {}, {}", path, method, ipAddress);
        return path.equals("/api/v1/auth/signup") ||
            path.equals("/api/v1/auth/login") ||
            (path.equals("/api/v1/auth/my") && method.equals("DELETE")) ||
            path.equals("/actuator/prometheus")
            ;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        log.info(request.getRequestURI());
        log.info(request.getQueryString());
        log.info("request uri: {}", request.getRequestURI());
        log.info("request method: {}", request.getMethod());
        log.info("request headers: {}", request.getHeaderNames());
        log.info("request queryString: {}", request.getQueryString());

        log.info("doFilterInternal");
        String accessToken = jwtUtil.getAccessTokenFromHeader(request);
        String refreshToken = cookieUtil.getRefreshTokenFromCookie(request);

        log.info("accessToken: {}", accessToken);
        log.info("refreshToken: {}", refreshToken);

        if (StringUtils.hasText(accessToken) && jwtUtil.validateToken(accessToken)) {
            Claims claims = jwtUtil.getUserInfoFromToken(accessToken);
            try {
                setAuthentication(claims.getSubject());
            } catch (Exception e) {
                log.error("setAuthentication Failed: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }

        log.error("액세스 토큰 유효 X");

        if (StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken)) {
            Claims refreshClaims = jwtUtil.getUserInfoFromToken(refreshToken);
            String username = refreshClaims.getSubject();
            String roleClaim = refreshClaims.get("role", String.class);
            AuthRole role = AuthRole.valueOf(roleClaim);

            log.info("expirePassport");
            passportService.expirePassportByRefreshToken(refreshToken);

            String newAccessToken = jwtUtil.generateAccessToken(username, role);
            String newRefreshToken = jwtUtil.generateRefreshToken(username, role);

            response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
            cookieUtil.addRefreshTokenToCookie(response,
                jwtUtil.getTokenWithoutBearer(newRefreshToken));

            passportService.createAndStoreNewPassportByRefreshToken(refreshToken, newRefreshToken);

            try {
                setAuthentication(username);
            } catch (Exception e) {
                log.error("setAuthentication Failed: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            filterChain.doFilter(request, response);

            return;
        }

        log.error("리프레쉬 토큰 유효 X");

        throw new JwtException(JwtExceptionCode.JWT_INVALID_TOKEN);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username) {
        AuthDetails authDetails = authDetailsService.loadAuthByUsername(username);

        return new UsernamePasswordAuthenticationToken(
            authDetails,
            null,
            authDetails.getAuthorities());
    }
}
