package on.ssgdeal.user_service.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.user_service.domain.entity.Destination;
import on.ssgdeal.user_service.domain.repository.DestinationRepository;
import on.ssgdeal.user_service.infrastructure.persistence.jpa.DestinationJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DestinationRepositoryImpl implements DestinationRepository {

    private final DestinationJpaRepository jpaRepository;

    @Override
    public Destination save(Destination destination) {
        return jpaRepository.save(destination);
    }

    @Override
    public void delete(Destination destination) {
        jpaRepository.delete(destination);
    }

    @Override
    public Optional<Destination> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Destination> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public Optional<Destination> findByIdAndUserId(Long id, Long userId) {
        return jpaRepository.findByIdAndUserId(id, userId);
    }
}
