package on.ssgdeal.user_service.application.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.user_service.application.dto.destination.CreateMyDestinationRequestDto;
import on.ssgdeal.user_service.application.dto.destination.UpdateMyDestinationRequestDto;
import on.ssgdeal.user_service.application.dto.destination.ValidateDestinationsRequestDto;
import on.ssgdeal.user_service.domain.entity.Destination;
import on.ssgdeal.user_service.domain.entity.Destination.CreateDestinationDto;
import on.ssgdeal.user_service.domain.repository.DestinationRepository;
import on.ssgdeal.user_service.exception.destination.DestinationException;
import on.ssgdeal.user_service.presentation.external.dto.destination.CreateMyDestinationResponse;
import on.ssgdeal.user_service.presentation.external.dto.destination.FindAllMyDestinationsResponse;
import on.ssgdeal.user_service.presentation.external.dto.destination.FindMyDestinationResponse;
import on.ssgdeal.user_service.presentation.external.dto.destination.UpdateMyDestinationResponse;
import on.ssgdeal.user_service.presentation.internal.dto.ValidateDestinationsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;
    private final PassportUtil passportUtil;

    @Override
    public FindAllMyDestinationsResponse findAllMy(HttpServletRequest httpServletRequest) {
        Passport passport = passportUtil.getPassportBy(httpServletRequest);
        List<Destination> myDestinations = destinationRepository.findByUserId(passport.getUserId());

        if (myDestinations.isEmpty()) {
            throw new DestinationException.DestinationNotFoundException();
        }

        return FindAllMyDestinationsResponse.from(passport, myDestinations);
    }

    @Override
    @Transactional
    public CreateMyDestinationResponse createMy(
        HttpServletRequest httpServletRequest,
        CreateMyDestinationRequestDto dto
    ) {
        Passport passport = passportUtil.getPassportBy(httpServletRequest);

        CreateDestinationDto entityDto = dto.toCreateDestinationDto();

        Destination destination = Destination.create(passport, entityDto);
        Destination savedDestination = destinationRepository.save(destination);

        return CreateMyDestinationResponse.from(savedDestination.getId());
    }

    @Override
    @Transactional
    public UpdateMyDestinationResponse updateMy(
        HttpServletRequest httpServletRequest,
        UpdateMyDestinationRequestDto dto
    ) {
        Passport passport = passportUtil.getPassportBy(httpServletRequest);
        Long userId = passport.getUserId();

        Destination destination = findByIdAndUserIdOrElseThrow(dto.destinationId(), userId);
        destination.update(dto.toUpdateDestinationDto());

        Destination updatedDestination = destinationRepository.save(destination);

        return UpdateMyDestinationResponse.from(updatedDestination);
    }

    @Override
    public void deleteMy(
        HttpServletRequest httpServletRequest,
        Long destinationId
    ) {
        Passport passport = passportUtil.getPassportBy(httpServletRequest);
        Long userId = passport.getUserId();

        Destination destination = findByIdAndUserIdOrElseThrow(destinationId, userId);

        destinationRepository.delete(destination);
    }

    @Override
    public FindMyDestinationResponse findMy(
        HttpServletRequest httpServletRequest,
        Long destinationId
    ) {
        Passport passport = passportUtil.getPassportBy(httpServletRequest);
        Long userId = passport.getUserId();

        Destination destination = findByIdAndUserIdOrElseThrow(destinationId, userId);

        return FindMyDestinationResponse.from(passport, destination);
    }

    @Override
    public ValidateDestinationsResponse validateMyDestinations(
        HttpServletRequest httpServletRequest,
        ValidateDestinationsRequestDto dto
    ) {
        Passport passport = passportUtil.getPassportBy(httpServletRequest);
        Long userId = passport.getUserId();

        Destination destination = findByIdAndUserIdOrElseThrow(dto.destinationId(), userId);

        return ValidateDestinationsResponse.from(destination);
    }


    private Destination findByIdAndUserIdOrElseThrow(Long destinationId, Long userId) {
        return destinationRepository
            .findByIdAndUserId(destinationId, userId)
            .orElseThrow(DestinationException.DestinationNotFoundException::new);
    }

    private Destination findByIdOrElseThrow(Long destinationId) {
        return destinationRepository
            .findById(destinationId)
            .orElseThrow(DestinationException.DestinationNotFoundException::new);
    }
}
