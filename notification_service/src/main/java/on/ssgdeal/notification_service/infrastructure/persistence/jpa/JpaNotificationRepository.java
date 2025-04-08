package on.ssgdeal.notification_service.infrastructure.persistence.jpa;

import on.ssgdeal.notification_service.domain.entity.Notification;
import on.ssgdeal.notification_service.domain.repository.NotificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNotificationRepository extends JpaRepository<Notification, Long>, NotificationRepository {
}
