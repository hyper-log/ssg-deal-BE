package on.ssgdeal.common.messaging.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@SQLRestriction("success = false")
public class Outbox {

    private static final int MAX_RETRY_COUNT = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AggregateType aggregateType;

    private Long aggregateId;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private String topic;

    private int retryCount;

    private boolean success;

    public static Outbox create(
        String topic,
        AggregateType aggregateType,
        Long aggregateId,
        String payload
    ) {
        return Outbox.builder()
            .topic(topic)
            .aggregateType(aggregateType)
            .aggregateId(aggregateId)
            .payload(payload)
            .retryCount(0)
            .success(false)
            .build();
    }

    public void increaseRetryCount() {
        this.retryCount++;
    }

    public boolean isOverRetryCount() {
        return this.retryCount > MAX_RETRY_COUNT;
    }

    public void success() {
        this.success = true;
    }

    public enum AggregateType {
        ORDER,
        PAYMENT,
        ;
    }
}
