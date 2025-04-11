package on.ssgdeal.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.annotation.RoleCheck;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.common.global.exception.CommonException.CommonForbiddenException;
import on.ssgdeal.common.global.exception.CommonException.CommonPassportNotFoundException;
import on.ssgdeal.common.global.exception.CommonException.CommonRequestNotFoundException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleCheckAspect {

    private final PassportUtil passportUtil;

    @Around("@annotation(roleCheck)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RoleCheck roleCheck) throws Throwable {
        String[] requiredRoles = roleCheck.value();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new CommonRequestNotFoundException();
        }
        HttpServletRequest request = attributes.getRequest();
        System.out.println("ididid" + request.getHeader("X-Passport-Id"));
        Passport passport = passportUtil.getPassportBy(request);

        if (passport == null) {
            throw new CommonPassportNotFoundException();
        }

        if (Arrays.stream(requiredRoles)
            .noneMatch(role -> role.equalsIgnoreCase(passport.getRole().toString()))) {
            throw new CommonForbiddenException();
        }

        return joinPoint.proceed();
    }

}
