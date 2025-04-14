package on.ssgdeal.notification_service.infrastructure.persistence.jpa;

import on.ssgdeal.notification_service.domain.entity.NotificationTemplate;
import on.ssgdeal.notification_service.domain.enums.NotificationTemplateType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationTemplateJpaRepository extends JpaRepository<NotificationTemplate, Long> {

    Optional<NotificationTemplate> findByType(NotificationTemplateType type);
}
