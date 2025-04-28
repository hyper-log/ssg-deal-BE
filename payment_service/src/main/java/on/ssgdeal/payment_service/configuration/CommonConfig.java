package on.ssgdeal.payment_service.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {
    "on.ssgdeal.common"
})
@EntityScan(basePackages = {
    "on.ssgdeal.payment_service",
    "on.ssgdeal.common.messaging.domain.entity"
})
@EnableJpaRepositories(basePackages = {
    "on.ssgdeal.payment_service",
    "on.ssgdeal.common.messaging.domain.repository"
})
public class CommonConfig {

}
