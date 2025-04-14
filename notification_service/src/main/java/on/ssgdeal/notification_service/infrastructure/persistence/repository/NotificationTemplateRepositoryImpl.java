package on.ssgdeal.notification_service.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.notification_service.domain.entity.NotificationTemplate;
import on.ssgdeal.notification_service.domain.enums.NotificationTemplateType;
import on.ssgdeal.notification_service.domain.repository.NotificationTemplateRepository;
import on.ssgdeal.notification_service.infrastructure.persistence.jpa.NotificationTemplateJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationTemplateRepositoryImpl implements NotificationTemplateRepository {

    private final NotificationTemplateJpaRepository notificationTemplateJpaRepository;


    @Override
    public NotificationTemplate save(NotificationTemplate template) {
        return notificationTemplateJpaRepository.save(template);
    }
    @Override
    public Optional<NotificationTemplate> findByType(NotificationTemplateType type) {
        return notificationTemplateJpaRepository.findByType(type);
    }
}
