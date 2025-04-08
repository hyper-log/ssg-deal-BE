package on.ssgdeal.auth_service.application.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import on.ssgdeal.auth_service.application.service.dto.SignupAuthRequestDto;
import on.ssgdeal.auth_service.presentation.external.dto.SignupAuthResponse;
import on.ssgdeal.auth_service.presentation.internal.dto.ReissueTokensAuthResponse;
import on.ssgdeal.auth_service.presentation.internal.dto.ValidateAuthResponse;


public interface AuthService {

    SignupAuthResponse signup(SignupAuthRequestDto authRequestDto);

    ValidateAuthResponse validate(HttpServletRequest request);

    void logout(HttpServletRequest request);

    ReissueTokensAuthResponse reIssueTokens(HttpServletRequest request,
        HttpServletResponse response);


    void withdrawUserByPassport(HttpServletRequest request);

    void withdrawUserByUserId(Long userId, HttpServletRequest request);
}
