package on.ssgdeal.order_service.configuration;

import on.ssgdeal.common.command.ScopedCommandInvoker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SagaConfig {
    
    @Bean
    public ScopedCommandInvoker scopedCommandInvoker() {
        return new ScopedCommandInvoker();
    }

}
