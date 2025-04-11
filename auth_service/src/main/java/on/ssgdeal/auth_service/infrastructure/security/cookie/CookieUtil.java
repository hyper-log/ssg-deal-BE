package on.ssgdeal.auth_service.infrastructure.security.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "CookieUtil")
public class CookieUtil {

    private static final String REFRESH_TOKEN = "refreshToken";

    public void addRefreshTokenToCookie(
        HttpServletResponse response,
        String refreshToken
    ) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, refreshToken)
            .httpOnly(true)
            .path("/api/v1")
            .build();

        log.info("Add refresh token to cookie: {}", cookie);

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void removeRefreshTokenFromCookie(
        HttpServletResponse response
    ) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, "")
            .httpOnly(true)
            .path("/")
            .maxAge(0)
            .build();

        log.info("Remove refresh token from cookie: {}", cookie);

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public String getRefreshTokenFromCookie(
        HttpServletRequest request
    ) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                .filter(cookie -> REFRESH_TOKEN.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
        }
        return null;
    }

}
