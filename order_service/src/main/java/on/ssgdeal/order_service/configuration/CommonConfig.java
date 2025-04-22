package on.ssgdeal.order_service.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "on.ssgdeal.common"
})
@EntityScan(basePackages = {"on.ssgdeal.order_service",
    "on.ssgdeal.common.messaging.domain.entity"})
public class CommonConfig {

}
