package on.ssgdeal.notification_service.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.notification_service.domain.entity.Notification;
import on.ssgdeal.notification_service.domain.entity.NotificationTemplate;
import on.ssgdeal.notification_service.domain.enums.NotificationTemplateType;
import on.ssgdeal.notification_service.domain.repository.NotificationRepository;
import on.ssgdeal.notification_service.domain.repository.NotificationTemplateRepository;
import on.ssgdeal.notification_service.infrastructure.persistence.jpa.JpaNotificationRepository;
import on.ssgdeal.notification_service.infrastructure.persistence.jpa.JpaNotificationTemplateRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationTemplateRepositoryImpl implements NotificationTemplateRepository {

    private final JpaNotificationTemplateRepository jpaNotificationTemplateRepository;


    @Override
    public NotificationTemplate save(NotificationTemplate template) {
        return jpaNotificationTemplateRepository.save(template);
    }
    @Override
    public Optional<NotificationTemplate> findByType(NotificationTemplateType type) {
        return jpaNotificationTemplateRepository.findByType(type);
    }
}
