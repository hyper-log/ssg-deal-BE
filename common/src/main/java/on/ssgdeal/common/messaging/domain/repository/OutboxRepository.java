package on.ssgdeal.common.messaging.domain.repository;

import java.util.List;
import on.ssgdeal.common.messaging.domain.entity.Outbox;
import on.ssgdeal.common.messaging.domain.entity.Outbox.AggregateType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    List<Outbox> findByAggregateType(AggregateType aggregateType);
}
