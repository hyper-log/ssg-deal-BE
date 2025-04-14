package on.ssgdeal.user_service.infrastructure.persistence.jpa;

import java.util.Optional;
import on.ssgdeal.user_service.domain.entity.User;
import on.ssgdeal.user_service.domain.vo.SlackEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Boolean existsBySlackEmail(SlackEmail slackEmail);

}
