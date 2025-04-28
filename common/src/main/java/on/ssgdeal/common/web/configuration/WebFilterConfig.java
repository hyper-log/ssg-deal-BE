package on.ssgdeal.common.web.configuration;

import on.ssgdeal.common.web.filter.MdcServletFilter;
import on.ssgdeal.common.web.filter.PassportFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebFilterConfig {

    private static final Integer MDC_FILTER_ORDER = 1;
    private static final Integer PASSPORT_FILTER_ORDER = 2;

    @Bean
    public FilterRegistrationBean<MdcServletFilter> mdcFilterRegistration(MdcServletFilter filter) {
        FilterRegistrationBean<MdcServletFilter> reg = new FilterRegistrationBean<>(filter);
        reg.setOrder(MDC_FILTER_ORDER);
        reg.addUrlPatterns("/*");
        return reg;
    }

    @Bean
    public FilterRegistrationBean<PassportFilter> passportFilterRegistration(
        PassportFilter filter) {
        FilterRegistrationBean<PassportFilter> reg = new FilterRegistrationBean<>(filter);
        reg.setOrder(PASSPORT_FILTER_ORDER);
        reg.addUrlPatterns("/*");
        return reg;
    }

}
