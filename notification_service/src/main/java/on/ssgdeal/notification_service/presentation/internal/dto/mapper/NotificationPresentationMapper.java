package on.ssgdeal.notification_service.presentation.internal.dto.mapper;

import on.ssgdeal.notification_service.application.service.dto.CreateNotificationRequestDto;
import on.ssgdeal.notification_service.presentation.internal.dto.CreateNotificationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationPresentationMapper {

    @Mapping(target = "receiverSlackEmail", source = "request.slackEmail")
    @Mapping(target = "senderSlackEmail", source = "senderSlackEmail")
    CreateNotificationRequestDto toNotificationRequestDto(CreateNotificationRequest request, String senderSlackEmail);

}
