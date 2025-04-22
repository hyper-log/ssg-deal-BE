package on.ssgdeal.notification_service.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.notification_service.domain.entity.Notification;
import on.ssgdeal.notification_service.domain.repository.NotificationRepository;
import on.ssgdeal.notification_service.infrastructure.persistence.jpa.NotificationJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public Notification save(Notification notification) {
        return notificationJpaRepository.save(notification);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationJpaRepository.findById(id);
    }

    @Override
    public List<Notification> findAll() {
        return notificationJpaRepository.findAll();
    }

    @Override
    public void deleteAll() {
        notificationJpaRepository.deleteAll();
    }
}
