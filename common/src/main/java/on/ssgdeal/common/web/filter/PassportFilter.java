package on.ssgdeal.common.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.common.mdc.PassportMdcContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class PassportFilter extends OncePerRequestFilter {

    private final PassportUtil passportUtil;

    @Override
    protected boolean shouldNotFilter(
        HttpServletRequest request
    ) throws ServletException {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        return isCreateUserRequest(uri, method)
            || isSignupAuthRequest(uri, method)
            || isLoginAuthRequest(uri, method);
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String passportId = request.getHeader(PassportUtil.PASSPORT_ID_HEADER);

        try (PassportMdcContext context = new PassportMdcContext(passportUtil, passportId)) {
            filterChain.doFilter(request, response);
        } catch (ServletException | IOException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException("Passport Filter processing failed - " + e.getMessage(), e);
        }
    }

    private static boolean isCreateUserRequest(String uri, String method) {
        return uri.contains("/internal")
            && uri.contains("/users")
            && method.equals("POST");
    }

    private static boolean isSignupAuthRequest(String uri, String method) {
        return uri.contains("/api")
            && uri.contains("/auth/signup")
            && method.equals("POST");
    }

    private static boolean isLoginAuthRequest(String uri, String method) {
        return uri.contains("/api")
            && uri.contains("/auth/login")
            && method.equals("POST");
    }
}
