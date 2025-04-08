package on.ssgdeal.auth_service.infrastructure.client.user_service;


import lombok.RequiredArgsConstructor;
import on.ssgdeal.auth_service.infrastructure.client.user_service.dto.UserCreateRequest;
import on.ssgdeal.auth_service.infrastructure.client.user_service.feign.UserFeignClient;
import on.ssgdeal.auth_service.infrastructure.client.user_service.feign.dto.UserCreateResponse;
import on.ssgdeal.auth_service.infrastructure.client.user_service.feign.dto.UserFindByIdResponse;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserClientService {

    private final UserFeignClient userFeignClient;

    public UserCreateResponse createUser(UserCreateRequest request) {
        ResponseEntity<CommonResponse<UserCreateResponse>> responseEntity =
            userFeignClient.createUser(request);

        CommonResponse<UserCreateResponse> commonResponse = responseEntity.getBody();
        if (commonResponse != null) {
            return commonResponse.data();
        } else {
            return null;
        }
    }

    public UserFindByIdResponse findUserById(Long userId) {
        ResponseEntity<CommonResponse<UserFindByIdResponse>> responseEntity =
            userFeignClient.findUserById(userId);

        CommonResponse<UserFindByIdResponse> commonResponse = responseEntity.getBody();
        if (commonResponse != null) {
            return commonResponse.data();
        } else {
            return null;
        }

    }

    public void withdrawUserByUserId(Long userId) {
        userFeignClient.deleteUserByUserId(userId);
    }
}
