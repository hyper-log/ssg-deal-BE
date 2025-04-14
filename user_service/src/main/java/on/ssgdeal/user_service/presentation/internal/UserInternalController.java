package on.ssgdeal.user_service.presentation.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.user_service.application.dto.user.CreateUserRequestDto;
import on.ssgdeal.user_service.application.service.UserService;
import on.ssgdeal.user_service.presentation.external.dto.user.CreateUserRequest;
import on.ssgdeal.user_service.presentation.external.dto.user.CreateUserResponse;
import on.ssgdeal.user_service.presentation.internal.dto.FindByIdUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j(topic = "InternalUserController")
@RequiredArgsConstructor
@RequestMapping("/internal/v1/users")
public class UserInternalController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<FindByIdUserResponse>> findUserById(
        @PathVariable("id") Long id
    ) {
        FindByIdUserResponse response = userService.findUserByIdInternal(id);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<CommonResponse<CreateUserResponse>> createUser(
        @RequestBody CreateUserRequest request
    ) {
        CreateUserRequestDto dto = request.toDto();
        CreateUserResponse response = userService.createUser(dto);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PutMapping("/withdraw/{id}")
    public ResponseEntity<CommonResponse<Void>> withdrawUserByUserId(
        @PathVariable("id") Long id
    ) {
        log.info("withdrawUserByUserId, {}", id.toString());
        userService.withdrawUserByUserId(id);

        return ResponseEntity.ok(CommonResponse.success());
    }

}
