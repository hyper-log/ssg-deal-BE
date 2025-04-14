package on.ssgdeal.user_service.presentation.external.dto.destination;

import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.user_service.domain.entity.Destination;

public record FindMyDestinationResponse(
    String nickname,
    String slackEmail,
    Long destinationId,
    String destinationName,
    String address
) {

    public static FindMyDestinationResponse from(Passport passport, Destination destination) {
        return new FindMyDestinationResponse(
            passport.getNickname(),
            passport.getSlackEmail(),
            destination.getId(),
            destination.getName(),
            destination.getAddress().toString()
        );
    }

}
