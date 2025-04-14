package on.ssgdeal.user_service.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import on.ssgdeal.user_service.domain.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationJpaRepository extends JpaRepository<Destination, Long> {

    Optional<Destination> findById(Long id);

    List<Destination> findByUserId(Long userId);

    Optional<Destination> findByIdAndUserId(Long id, Long userId);

}
