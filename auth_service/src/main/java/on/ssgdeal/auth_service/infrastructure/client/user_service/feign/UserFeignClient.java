package on.ssgdeal.auth_service.infrastructure.client.user_service.feign;


import on.ssgdeal.auth_service.infrastructure.client.user_service.dto.UserCreateRequest;
import on.ssgdeal.auth_service.infrastructure.client.user_service.feign.dto.UserCreateResponse;
import on.ssgdeal.auth_service.infrastructure.client.user_service.feign.dto.UserFindByIdResponse;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @GetMapping("/internal/v1/users/{id}")
    ResponseEntity<CommonResponse<UserFindByIdResponse>> findUserById(
        @RequestHeader("X-Internal-Secret") String headerSecretKey,
        @PathVariable("id") Long id
    );

    @PostMapping("/api/v1/users")
    ResponseEntity<CommonResponse<UserCreateResponse>> createUser(
        @RequestHeader("X-Internal-Secret") String headerSecretKey,
        @RequestBody UserCreateRequest request);

    @PutMapping("/internal/v1/users/withdraw/{id}")
    ResponseEntity<CommonResponse<Void>> deleteUserByUserId(
        @RequestHeader("X-Internal-Secret") String headerSecretKey,
        @PathVariable("id") Long id
    );

}
