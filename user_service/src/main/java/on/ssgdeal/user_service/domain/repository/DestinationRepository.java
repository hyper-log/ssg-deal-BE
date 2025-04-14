package on.ssgdeal.user_service.domain.repository;

import java.util.List;
import java.util.Optional;
import on.ssgdeal.user_service.domain.entity.Destination;

public interface DestinationRepository {

    Destination save(Destination destination);

    void delete(Destination destination);

    Optional<Destination> findById(Long id);

    List<Destination> findByUserId(Long userId);

    Optional<Destination> findByIdAndUserId(Long id, Long userId);
}
