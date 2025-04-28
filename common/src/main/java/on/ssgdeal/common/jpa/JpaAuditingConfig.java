package on.ssgdeal.common.jpa;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.mdc.MdcKey;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "loginUserAuditorAware")
@RequiredArgsConstructor
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<Long> loginUserAuditorAware() {
        return () -> {
            String userId = MDC.get(MdcKey.USER_ID.getKey());
            return Optional.of(Long.parseLong(userId));
        };
    }
}
