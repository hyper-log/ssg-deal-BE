package on.ssgdeal.notification_service.domain.repository;

import on.ssgdeal.notification_service.domain.entity.Notification;
import on.ssgdeal.notification_service.domain.entity.NotificationTemplate;
import on.ssgdeal.notification_service.domain.enums.NotificationTemplateType;

import java.util.Optional;

public interface NotificationTemplateRepository {
    Optional<NotificationTemplate> findByType(NotificationTemplateType type);

    NotificationTemplate save(NotificationTemplate notification);
    
}
