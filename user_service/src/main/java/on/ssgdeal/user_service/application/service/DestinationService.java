package on.ssgdeal.user_service.application.service;

import jakarta.servlet.http.HttpServletRequest;
import on.ssgdeal.user_service.application.dto.destination.CreateMyDestinationRequestDto;
import on.ssgdeal.user_service.application.dto.destination.UpdateMyDestinationRequestDto;
import on.ssgdeal.user_service.application.dto.destination.ValidateDestinationsRequestDto;
import on.ssgdeal.user_service.presentation.external.dto.destination.CreateMyDestinationResponse;
import on.ssgdeal.user_service.presentation.external.dto.destination.FindAllMyDestinationsResponse;
import on.ssgdeal.user_service.presentation.external.dto.destination.FindMyDestinationResponse;
import on.ssgdeal.user_service.presentation.external.dto.destination.UpdateMyDestinationResponse;
import on.ssgdeal.user_service.presentation.internal.dto.ValidateDestinationsResponse;

public interface DestinationService {

    FindAllMyDestinationsResponse findAllMy(HttpServletRequest httpServletRequest);

    CreateMyDestinationResponse createMy(
        HttpServletRequest httpServletRequest,
        CreateMyDestinationRequestDto dto);

    UpdateMyDestinationResponse updateMy(
        HttpServletRequest httpServletRequest,
        UpdateMyDestinationRequestDto dto);

    void deleteMy(HttpServletRequest httpServletRequest, Long destinationId);

    FindMyDestinationResponse findMy(HttpServletRequest httpServletRequest, Long destinationId);

    ValidateDestinationsResponse validateMyDestinations(
        HttpServletRequest httpServletRequest,
        ValidateDestinationsRequestDto dto);
}
