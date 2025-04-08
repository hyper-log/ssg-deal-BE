package on.ssgdeal.auth_service.presentation.external;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.auth_service.application.service.AuthServiceImpl;
import on.ssgdeal.auth_service.application.service.dto.SignupAuthRequestDto;
import on.ssgdeal.auth_service.presentation.external.dto.SignupAuthRequest;
import on.ssgdeal.auth_service.presentation.external.dto.SignupAuthResponse;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<SignupAuthResponse>> signup(
        @RequestBody SignupAuthRequest request
    ) {
        log.info("user signup request: {}", request);

        SignupAuthRequestDto requestDto = request.toDto();
        SignupAuthResponse response = authService.signup(requestDto);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<Void>> logout(
        HttpServletRequest request
    ) {
        log.info("logout request: {}", request);

        authService.logout(request);
        return ResponseEntity.ok(CommonResponse.success());
    }


    @DeleteMapping("/my")
    public ResponseEntity<CommonResponse<Void>> withdraw(
        HttpServletRequest request
    ) {
        log.info("withdraw user by request: {}", request);

        authService.withdrawUserByPassport(request);
        return ResponseEntity.ok(CommonResponse.success());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<CommonResponse<Void>> withdrawByUserId(
        @PathVariable Long userId,
        HttpServletRequest request
    ) {
        log.info("withdraw user by Id: {}", userId);

        authService.withdrawUserByUserId(userId, request);
        return ResponseEntity.ok(CommonResponse.success());
    }

}
