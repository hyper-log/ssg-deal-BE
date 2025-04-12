package on.ssgdeal.notification_service.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.notification_service.domain.entity.Notification;
import on.ssgdeal.notification_service.domain.repository.NotificationRepository;
import on.ssgdeal.notification_service.infrastructure.persistence.jpa.JpaNotificationRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final JpaNotificationRepository jpaNotificationRepository;

    @Override
    public Notification save(Notification notification) {
        return jpaNotificationRepository.save(notification);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return jpaNotificationRepository.findById(id);
    }

}
