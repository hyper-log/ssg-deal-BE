package on.ssgdeal.user_service.presentation.external.dto.destination;

import java.util.List;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.user_service.domain.entity.Destination;

public record FindAllMyDestinationsResponse(
    String nickname,
    String slackEmail,
    List<DestinationResponse> destinations
) {

    public static FindAllMyDestinationsResponse from(Passport passport,
        List<Destination> destinations) {
        return new FindAllMyDestinationsResponse(
            passport.getNickname(),
            passport.getSlackEmail(),
            destinations
                .stream()
                .map(DestinationResponse::from)
                .toList()
        );
    }

    public record DestinationResponse(
        Long destinationId,
        String destinationName,
        String address
    ) {

        public static DestinationResponse from(Destination destination) {
            return new DestinationResponse(
                destination.getId(),
                destination.getName(),
                destination.getAddress().toString()
            );
        }
    }
}


