package on.ssgdeal.gateway_service.infrastructure.filter;

import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.gateway_service.presentation.dto.CommonResponse;
import on.ssgdeal.gateway_service.presentation.internal.dto.AuthValidateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j(topic = "AuthGlobalFilter")
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final WebClient webClient;
    @Value("${spring.cloud.gateway.auth.login.endpoint}")
    private String loginUrl;
    @Value("${spring.cloud.gateway.auth.signup.endpoint}")
    private String signupUrl;
    @Value("${spring.cloud.gateway.auth.validate.endpoint}")
    private String validateEndpoint;

    public AuthGlobalFilter(
        WebClient.Builder webClientBuilder,
        @Value("${spring.cloud.gateway.auth.base.url}") String baseUrl
    ) {
        this.webClient = webClientBuilder
            .baseUrl(baseUrl)
            .build();
    }

    @Override
    public Mono<Void> filter(
        ServerWebExchange exchange,
        GatewayFilterChain chain
    ) {
        log.info("Auth Global Filter");
        String path = exchange.getRequest().getURI().getPath();

        log.info("Auth Requested path: {}", path);

        if (
            path.equals(loginUrl) || path.equals(signupUrl)
        ) {
            return chain.filter(exchange);
        }

        String accessToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (accessToken == null || accessToken.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            log.warn("Access Token is Empty");
            return exchange.getResponse().setComplete();
        }

        var refreshCookie = exchange.getRequest().getCookies().getFirst("refreshToken");
        if (refreshCookie == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            log.warn("Refresh Token is Empty");
            return exchange.getResponse().setComplete();
        }
        String refreshToken = refreshCookie.getValue();

        return webClient.get()
            .uri(validateEndpoint)
            .header("Authorization", accessToken)
            .cookie("refreshToken", refreshToken)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<CommonResponse<AuthValidateResponse>>() {
            })
            .flatMap(commonResponse -> {
                if (commonResponse.data() == null) {
                    log.error("Common Response Data Is Null");
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
                String passportId = commonResponse.data().passportId();
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .headers(httpHeaders -> {
                        httpHeaders.remove("Authorization");
                        httpHeaders.add("X-Passport-Id", passportId);
                    })
                    .build();
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            })
            .onErrorResume(ex -> {
                log.error(ex.getMessage());
                log.error("Request URL : {} ", exchange.getRequest().getURI());
                log.error("WebClientInfo : {} ", validateEndpoint);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            });
    }

    @Override
    public int getOrder() {
        return -1;
    }

}
