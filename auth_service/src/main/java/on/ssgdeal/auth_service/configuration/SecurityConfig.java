package on.ssgdeal.auth_service.configuration;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.auth_service.application.service.PassportService;
import on.ssgdeal.auth_service.infrastructure.security.cookie.CookieUtil;
import on.ssgdeal.auth_service.infrastructure.security.details.AuthDetailsServiceImpl;
import on.ssgdeal.auth_service.infrastructure.security.jwt.JwtAuthenticationFilter;
import on.ssgdeal.auth_service.infrastructure.security.jwt.JwtAuthorizationFilter;
import on.ssgdeal.auth_service.infrastructure.security.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final AuthDetailsServiceImpl authDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final PassportService passportService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthentiactionFilter() throws Exception {
        return JwtAuthenticationFilter.builder()
            .jwtUtil(jwtUtil)
            .cookieUtil(cookieUtil)
            .authenticationManager(authenticationConfiguration.getAuthenticationManager())
            .passportService(passportService)
            .build();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return JwtAuthorizationFilter.builder()
            .jwtUtil(jwtUtil)
            .cookieUtil(cookieUtil)
            .authDetailsService(authDetailsService)
            .passportService(passportService)
            .build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(
                    "/",
                    "/api/v1/auth/signup",
                    "/api/v1/auth/login"
                ).permitAll()
                .requestMatchers(
                    "/actuator/prometheus",
                    "/actuator/health",
                    "/actuator/info"
                ).permitAll()
                .requestMatchers(
                    "/internal/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthentiactionFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);

        return http.build();
    }

}
