package on.ssgdeal.notification_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.notification_service.domain.enums.NotificationStatus;
import on.ssgdeal.notification_service.domain.enums.NotificationTemplateType;
import on.ssgdeal.notification_service.domain.vo.SlackEmail;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class NotificationRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @AttributeOverride(name = "value", column = @Column(name = "receiver_slack_email", nullable = false))
    @Embedded
    private SlackEmail receiverSlackEmail;

    @Column(name = "send_at", nullable = false)
    private LocalDateTime sendAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "template_type", nullable = false)
    private NotificationTemplateType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    public static NotificationRecord create(Notification notification, NotificationStatus status) {
        return NotificationRecord.builder()
                .notification(notification)
                .type(notification.getType())
                .receiverSlackEmail(notification.getReceiverSlackEmail())
                .sendAt(notification.getSendAt())
                .status(status)
                .build();
    }

}