package on.ssgdeal.user_service.presentation.external.dto.destination;

import java.util.List;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.user_service.domain.entity.Destination;

public record GetMyDestinationsResponse(
    String nickname,
    String slackEmail,
    List<DestinationResponse> destinations
) {

    public static GetMyDestinationsResponse from(Passport passport,
        List<Destination> destinations) {
        return new GetMyDestinationsResponse(
            passport.getNickname(),
            passport.getSlackEmail(),
            destinations
                .stream()
                .map(DestinationResponse::from)
                .toList()
        );
    }

    private record DestinationResponse(
        String address,
        String destinationName
    ) {

        private static DestinationResponse from(Destination destination) {
            return new DestinationResponse(
                destination.getAddress().toString(),
                destination.getName()
            );
        }
    }
}


