package on.ssgdeal.notification_service.infrastructure.persistence.jpa;

import on.ssgdeal.notification_service.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {

}
