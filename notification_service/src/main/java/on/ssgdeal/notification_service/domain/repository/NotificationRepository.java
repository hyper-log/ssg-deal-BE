package on.ssgdeal.notification_service.domain.repository;

import on.ssgdeal.notification_service.domain.entity.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findById(Long id);
    List<Notification> findAll();
    void deleteAll();
}
