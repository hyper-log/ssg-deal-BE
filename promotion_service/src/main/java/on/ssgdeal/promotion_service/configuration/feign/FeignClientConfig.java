package on.ssgdeal.promotion_service.configuration.feign;

import feign.RequestInterceptor;
import feign.Retryer;
import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
import on.ssgdeal.common.auth.passport.PassportUtil;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@EnableFeignClients(basePackages = "on.ssgdeal.promotion_service.infrastructure.client")
public class FeignClientConfig {

    @Bean
    public Retryer.Default feignRetryer() {
        long period = 100L;
        long duration = 3L;
        int maxAttempts = 5;
        return new Retryer.Default(period, TimeUnit.SECONDS.toMillis(duration), maxAttempts);
    }

    @Bean
    public FeignFormatterRegistrar dateTimeFormatterRegistrar() {
        return registry -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setUseIsoFormat(true);
            registrar.registerFormatters(registry);
        };
    }

    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        return template -> {
            ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String passportId = request.getHeader(PassportUtil.PASSPORT_ID_HEADER);
                if (passportId != null) {
                    template.header(PassportUtil.PASSPORT_ID_HEADER, passportId);
                }
            }
        };
    }
}
