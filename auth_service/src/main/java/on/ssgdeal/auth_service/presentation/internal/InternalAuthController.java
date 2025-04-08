package on.ssgdeal.auth_service.presentation.internal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.auth_service.application.service.AuthServiceImpl;
import on.ssgdeal.auth_service.presentation.internal.dto.ReissueTokensAuthResponse;
import on.ssgdeal.auth_service.presentation.internal.dto.ValidateAuthResponse;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/internal/v1/auth")
public class InternalAuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse<ReissueTokensAuthResponse>> reissueRefreshToken(
        HttpServletRequest servletRequest,
        HttpServletResponse servletResponse
    ) {
        log.info("reissue refresh token request: {}", servletRequest);

        ReissueTokensAuthResponse response = authService.reIssueTokens(servletRequest,
            servletResponse);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/validate")
    public ResponseEntity<CommonResponse<ValidateAuthResponse>> validate(
        HttpServletRequest request
    ) {
        log.info("validate request: {}", request);

        ValidateAuthResponse response = authService.validate(request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

}
