package on.ssgdeal.user_service.presentation.internal;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.user_service.application.dto.destination.ValidateDestinationsRequestDto;
import on.ssgdeal.user_service.application.service.DestinationService;
import on.ssgdeal.user_service.presentation.internal.dto.ValidateDestinationsRequest;
import on.ssgdeal.user_service.presentation.internal.dto.ValidateDestinationsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j(topic = "InternalUserController")
@RequiredArgsConstructor
@RequestMapping("/internal/v1/users/destinations")
public class DestinationInternalController {

    private final DestinationService destinationService;

    @PostMapping("/validations")
    public ResponseEntity<CommonResponse<ValidateDestinationsResponse>> validateMyDestinations(
        HttpServletRequest httpServletRequest,
        @RequestBody ValidateDestinationsRequest request
    ) {
        ValidateDestinationsRequestDto dto = request.toDto();
        var response = destinationService.validateMyDestinations(httpServletRequest, dto);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

}
