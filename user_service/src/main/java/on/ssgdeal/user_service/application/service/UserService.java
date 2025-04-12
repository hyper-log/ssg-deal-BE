package on.ssgdeal.user_service.application.service;

import jakarta.servlet.http.HttpServletRequest;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.user_service.application.dto.user.CreateUserRequestDto;
import on.ssgdeal.user_service.application.dto.user.SearchUserRequestDto;
import on.ssgdeal.user_service.application.dto.user.UpdateUserAdminRequestDto;
import on.ssgdeal.user_service.application.dto.user.UpdateUserRequestDto;
import on.ssgdeal.user_service.presentation.external.dto.user.CreateUserResponse;
import on.ssgdeal.user_service.presentation.external.dto.user.FindSlackEmailByIdResponse;
import on.ssgdeal.user_service.presentation.external.dto.user.SearchUserResponse;
import on.ssgdeal.user_service.presentation.external.dto.user.UpdateUserAdminResponse;
import on.ssgdeal.user_service.presentation.external.dto.user.UpdateUserResponse;
import on.ssgdeal.user_service.presentation.internal.dto.FindByIdUserResponse;
import on.ssgdeal.user_service.presentation.internal.dto.FindMyUserResponse;

public interface UserService {

    CreateUserResponse createUser(CreateUserRequestDto requestDto);

    FindByIdUserResponse findUserById(Long id);

    FindMyUserResponse findMyUser(HttpServletRequest request);

    PageDto<SearchUserResponse> searchUser(SearchUserRequestDto requestDto);

    UpdateUserResponse updateUser(UpdateUserRequestDto requestDto, HttpServletRequest request);

    UpdateUserAdminResponse updateUserAdmin(UpdateUserAdminRequestDto dto);

    FindByIdUserResponse findUserByIdInternal(Long id);

    void withdrawUserByUserId(Long id);

    FindSlackEmailByIdResponse getSlackEmailById(Long id);

}
