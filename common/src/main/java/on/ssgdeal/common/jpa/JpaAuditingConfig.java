package on.ssgdeal.common.jpa;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "loginUserAuditorAware")
@RequiredArgsConstructor
public class JpaAuditingConfig {

    private final HttpServletRequest httpServletRequest;
    private final PassportUtil passportUtil;

    private static boolean isCreateUserRequest(String requestUri, String requestMethod) {
        return requestUri.contains("/internal") &&
            requestUri.contains("/users") &&
            requestMethod.equals("POST");
    }

    private static boolean isSignupAuthRequest(String requestUri, String requestMethod) {
        return requestUri.contains("/api") &&
            requestUri.contains("/auth/signup") &&
            requestMethod.equals("POST");
    }

    private static boolean isLoginAuthRequest(String requestUri, String requestMethod) {
        return requestUri.contains("/api") &&
            requestUri.contains("/auth/login") &&
            requestMethod.equals("POST");
    }

    @Bean
    public AuditorAware<Long> loginUserAuditorAware() {
        return () -> {
            String requestUri = httpServletRequest.getRequestURI();
            String requestMethod = httpServletRequest.getMethod();

            if (
                isCreateUserRequest(requestUri, requestMethod) ||
                    isSignupAuthRequest(requestUri, requestMethod) ||
                    isLoginAuthRequest(requestUri, requestMethod)
            ) {
                return Optional.empty();
            }

            Passport passport = passportUtil.getPassportBy(httpServletRequest);
            return Optional.of(passport.getUserId());
        };
    }
}

