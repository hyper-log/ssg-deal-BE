package on.ssgdeal.auth_service.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.auth_service.exception.JwtException;
import on.ssgdeal.auth_service.exception.JwtExceptionCode;
import on.ssgdeal.common.auth.enums.AuthRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "JwtUtil")
@RequiredArgsConstructor
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_ROLE_KEY = "role";

    private final JwtBlacklistRepository jwtBlacklistRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.access.expiration}")
    private long accessTokenExpirationTime;
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpirationTime;
    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateAccessToken(
        String username,
        AuthRole authRole
    ) {

        return generateToken(username, authRole, accessTokenExpirationTime);
    }

    public String generateRefreshToken(
        String username,
        AuthRole authRole
    ) {

        return generateToken(username, authRole, refreshTokenExpirationTime);
    }

    public String generateToken(
        String username,
        AuthRole authRole,
        Long expiration
    ) {

        Date currentTime = new Date();
        Date expirationDate = new Date(currentTime.getTime() + expiration);

        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_ROLE_KEY, authRole)
                .setExpiration(expirationDate)
                .setIssuedAt(currentTime)
                .signWith(key)
                .compact();
    }

    public String getAccessTokenFromHeader(
        HttpServletRequest request
    ) {
        log.info("getAccessTokenFromHeader, {}", request.getHeader(AUTHORIZATION_HEADER));
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return getTokenWithoutBearer(bearerToken);
        }
        return null;
    }

    public boolean validateToken(String token) {
        if (isTokenBlacklisted(token)) {
            throw new JwtException(JwtExceptionCode.JWT_INVALID_TOKEN);
        }

        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error(e.getMessage());
            throw new JwtException(JwtExceptionCode.JWT_INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error(e.getMessage());
            throw new JwtException(JwtExceptionCode.JWT_UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new JwtException(JwtExceptionCode.JWT_CLAIMS_IS_EMPTY);
        }

        return true;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getTokenWithoutBearer(String token) {
        return token.substring(BEARER_PREFIX.length());
    }

    public void blacklist(String token) {
        jwtBlacklistRepository.addBlacklist(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return jwtBlacklistRepository.isBlacklisted(token);
    }
}