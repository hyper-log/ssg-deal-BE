package on.ssgdeal.notification_service.domain.repository;

import on.ssgdeal.notification_service.domain.entity.Notification;

public interface NotificationRepository {
    Notification save(Notification notification);
}
