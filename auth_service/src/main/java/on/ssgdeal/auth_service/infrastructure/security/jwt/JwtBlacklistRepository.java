package on.ssgdeal.auth_service.infrastructure.security.jwt;

import org.springframework.stereotype.Component;

@Component
public interface JwtBlacklistRepository {

    public void addBlacklist(String token);

    public void removeBlacklist(String token);

    public boolean isBlacklisted(String token);

}
