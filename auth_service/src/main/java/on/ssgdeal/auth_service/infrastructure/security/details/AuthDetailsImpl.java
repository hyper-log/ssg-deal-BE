package on.ssgdeal.auth_service.infrastructure.security.details;

import java.util.ArrayList;
import java.util.Collection;
import on.ssgdeal.auth_service.domain.entity.Auth;
import on.ssgdeal.common.auth.enums.AuthRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthDetailsImpl implements AuthDetails {

    private final Auth auth;

    public AuthDetailsImpl(Auth auth) {
        this.auth = auth;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        AuthRole authRole = auth.getRole();
        String authority = authRole.name();

        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(grantedAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return auth.getPassword();
    }

    @Override
    public String getUsername() {
        return auth.getUsername().toString();
    }

    public AuthRole getRole() {
        return auth.getRole();
    }

    public Long getUserId() {
        return auth.getUserId();
    }

}
