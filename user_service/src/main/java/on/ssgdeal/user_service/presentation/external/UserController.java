package on.ssgdeal.user_service.presentation.external;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.annotation.RoleCheck;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.user_service.application.dto.user.SearchUserRequestDto;
import on.ssgdeal.user_service.application.dto.user.UpdateUserAdminRequestDto;
import on.ssgdeal.user_service.application.dto.user.UpdateUserRequestDto;
import on.ssgdeal.user_service.application.service.UserService;
import on.ssgdeal.user_service.presentation.external.dto.user.FindSlackEmailByIdResponse;
import on.ssgdeal.user_service.presentation.external.dto.user.SearchUserResponse;
import on.ssgdeal.user_service.presentation.external.dto.user.UpdateUserAdminRequest;
import on.ssgdeal.user_service.presentation.external.dto.user.UpdateUserAdminResponse;
import on.ssgdeal.user_service.presentation.external.dto.user.UpdateUserRequest;
import on.ssgdeal.user_service.presentation.external.dto.user.UpdateUserResponse;
import on.ssgdeal.user_service.presentation.internal.dto.FindByIdUserResponse;
import on.ssgdeal.user_service.presentation.internal.dto.FindMyUserResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PatchMapping("/my")
    public ResponseEntity<CommonResponse<UpdateUserResponse>> updateUser(
        HttpServletRequest request,
        @RequestBody UpdateUserRequest updateUserRequest
    ) {
        UpdateUserRequestDto dto = updateUserRequest.toDto();
        UpdateUserResponse response = userService.updateUser(dto, request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/my")
    public ResponseEntity<CommonResponse<FindMyUserResponse>> findMyUser(
        HttpServletRequest request
    ) {
        FindMyUserResponse response = userService.findMyUser(request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @RoleCheck("MASTER")
    @GetMapping("/{id}/slack-email")
    public ResponseEntity<CommonResponse<FindSlackEmailByIdResponse>> getSlackEmailById(
        @PathVariable Long id
    ) {
        FindSlackEmailByIdResponse response = userService.getSlackEmailById(id);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @RoleCheck("MASTER")
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<FindByIdUserResponse>> findUserById(
        @PathVariable("id") Long id
    ) {
        FindByIdUserResponse response = userService.findUserById(id);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @RoleCheck("MASTER")
    @GetMapping
    public ResponseEntity<CommonResponse<PageDto<SearchUserResponse>>> getUser(
        @RequestParam(required = false) String nickname,
        @RequestParam(required = false) String slackEmail,
        @PageableDefault Pageable pageable
    ) {
        SearchUserRequestDto dto = SearchUserRequestDto.from(nickname, slackEmail, pageable);
        PageDto<SearchUserResponse> response = userService.searchUser(dto);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @RoleCheck("MASTER")
    @PatchMapping("/{id}")
    private ResponseEntity<CommonResponse<UpdateUserAdminResponse>> updateUserAdmin(
        @PathVariable Long id,
        @RequestBody UpdateUserAdminRequest updateUserAdminRequest
    ) {
        UpdateUserAdminRequestDto dto = updateUserAdminRequest.toDto(id);
        UpdateUserAdminResponse response = userService.updateUserAdmin(dto);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

}
