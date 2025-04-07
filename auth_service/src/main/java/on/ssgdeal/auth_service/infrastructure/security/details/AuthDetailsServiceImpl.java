package on.ssgdeal.auth_service.infrastructure.security.details;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.auth_service.domain.entity.Auth;
import on.ssgdeal.auth_service.domain.repository.AuthRepository;
import on.ssgdeal.auth_service.domain.vo.Username;
import on.ssgdeal.auth_service.exception.AuthException;
import on.ssgdeal.auth_service.exception.AuthExceptionCode;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthDetailsServiceImpl implements AuthDetailsService {

    private final AuthRepository authRepository;

    @Override
    public AuthDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Auth auth = authRepository.findByUsername(new Username(username))
            .orElseThrow(() -> new AuthException(AuthExceptionCode.AUTH_IS_NOT_FOUND));

        return new AuthDetailsImpl(auth);
    }

    @Override
    public AuthDetails loadAuthByUsername(String username) throws UsernameNotFoundException {

        return loadUserByUsername(username);
    }
}
