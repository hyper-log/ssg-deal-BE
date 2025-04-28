package on.ssgdeal.user_service.presentation.external;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.annotation.RoleCheck;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.user_service.application.dto.destination.CreateMyDestinationRequestDto;
import on.ssgdeal.user_service.application.dto.destination.UpdateMyDestinationRequestDto;
import on.ssgdeal.user_service.application.service.DestinationService;
import on.ssgdeal.user_service.presentation.external.dto.destination.CreateMyDestinationRequest;
import on.ssgdeal.user_service.presentation.external.dto.destination.CreateMyDestinationResponse;
import on.ssgdeal.user_service.presentation.external.dto.destination.FindAllMyDestinationsResponse;
import on.ssgdeal.user_service.presentation.external.dto.destination.FindMyDestinationResponse;
import on.ssgdeal.user_service.presentation.external.dto.destination.UpdateMyDestinationRequest;
import on.ssgdeal.user_service.presentation.external.dto.destination.UpdateMyDestinationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    @RoleCheck({"CONSUMER", "MASTER"})
    @GetMapping("/my")
    public ResponseEntity<CommonResponse<FindAllMyDestinationsResponse>> findAllMy(
        HttpServletRequest httpServletRequest
    ) {
        var response = destinationService.findAllMy(httpServletRequest);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @RoleCheck({"CONSUMER", "MASTER"})
    @PostMapping("/my")
    public ResponseEntity<CommonResponse<CreateMyDestinationResponse>> createMy(
        HttpServletRequest httpServletRequest,
        @RequestBody CreateMyDestinationRequest request
    ) {
        CreateMyDestinationRequestDto dto = request.toDto();
        var response = destinationService.createMy(httpServletRequest, dto);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @RoleCheck({"CONSUMER", "MASTER"})
    @PatchMapping("/my/{destinationId}")
    public ResponseEntity<CommonResponse<UpdateMyDestinationResponse>> updateMy(
        HttpServletRequest httpServletRequest,
        @PathVariable(name = "destinationId") Long destinationId,
        @RequestBody UpdateMyDestinationRequest request
    ) {
        UpdateMyDestinationRequestDto dto = request.toDto(destinationId);
        var response = destinationService.updateMy(httpServletRequest, dto);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @RoleCheck({"CONSUMER", "MASTER"})
    @DeleteMapping("/my/{destinationId}")
    public ResponseEntity<CommonResponse<Void>> deleteMy(
        HttpServletRequest httpServletRequest,
        @PathVariable(name = "destinationId") Long destinationId
    ) {
        destinationService.deleteMy(httpServletRequest, destinationId);

        return ResponseEntity.ok(CommonResponse.success());
    }

    @RoleCheck({"CONSUMER", "MASTER"})
    @GetMapping("/my/{destinationId}")
    public ResponseEntity<CommonResponse<FindMyDestinationResponse>> findMy(
        HttpServletRequest httpServletRequest,
        @PathVariable(name = "destinationId") Long destinationId
    ) {
        var response = destinationService.findMy(httpServletRequest, destinationId);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

}
