package on.ssgdeal.auth_service.presentation.internal;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.auth_service.application.service.AuthService;
import on.ssgdeal.auth_service.presentation.internal.dto.ValidateAuthResponse;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/gateway/v1/auth")
public class GatewayAuthController {

    private final AuthService authService;

    @GetMapping("/validate")
    public ResponseEntity<CommonResponse<ValidateAuthResponse>> validate(
        HttpServletRequest request
    ) {
        log.info("validate request: {}", request);

        ValidateAuthResponse response = authService.validate(request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

}
