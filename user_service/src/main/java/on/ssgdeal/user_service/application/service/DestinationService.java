package on.ssgdeal.user_service.application.service;

import jakarta.servlet.http.HttpServletRequest;
import on.ssgdeal.user_service.application.dto.destination.CreateMyDestinationDto;
import on.ssgdeal.user_service.presentation.external.dto.destination.CreateMyDestinationResponse;
import on.ssgdeal.user_service.presentation.external.dto.destination.GetMyDestinationsResponse;

public interface DestinationService {

    GetMyDestinationsResponse getMy(HttpServletRequest httpServletRequest);

    CreateMyDestinationResponse createMy(HttpServletRequest httpServletRequest,
        CreateMyDestinationDto dto);
}
