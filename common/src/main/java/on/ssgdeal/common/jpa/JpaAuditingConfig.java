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


    @Bean
    public AuditorAware<Long> loginUserAuditorAware() {
        return () -> {
            Passport passport = passportUtil.getPassportBy(httpServletRequest);
            return Optional.of(passport.getUserId());
        };

    }
}

