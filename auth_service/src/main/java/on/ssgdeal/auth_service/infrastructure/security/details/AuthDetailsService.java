package on.ssgdeal.auth_service.infrastructure.security.details;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthDetailsService extends UserDetailsService {

    AuthDetails loadAuthByUsername(String username) throws UsernameNotFoundException;
}
