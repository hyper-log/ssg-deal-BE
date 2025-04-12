package on.ssgdeal.notification_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationRequestDto;
import on.ssgdeal.notification_service.domain.entity.dto.CreateNotificationDto;
import on.ssgdeal.notification_service.domain.enums.NotificationStatus;
import on.ssgdeal.notification_service.domain.enums.NotificationTemplateType;
import on.ssgdeal.notification_service.domain.vo.SlackEmail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_template_id", nullable = false)
    private NotificationTemplate notificationTemplate;

    @AttributeOverride(name = "value", column = @Column(name = "sender_slack_email", nullable = false))
    @Embedded
    private SlackEmail senderSlackEmail;

    @AttributeOverride(name = "value", column = @Column(name = "receiver_slack_email", nullable = false))
    @Embedded
    private SlackEmail receiverSlackEmail;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "template_type", nullable = false)
    private NotificationTemplateType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    @Column(name = "send_at", nullable = false)
    private LocalDateTime sendAt;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    private List<NotificationRecord> records;

    public static Notification create(
            CreateNotificationDto notificationDto
    ) {
        Notification notification = Notification.builder()
                .notificationTemplate(notificationDto.template())
                .type(notificationDto.template().getType())
                .content(notificationDto.content())
                .senderSlackEmail(new SlackEmail(notificationDto.requestDto().senderSlackEmail()))
                .receiverSlackEmail(new SlackEmail(notificationDto.requestDto().receiverSlackEmail()))
                .sendAt(notificationDto.sendAt())
                .status(NotificationStatus.SUCCESS)
                .records(new ArrayList<>())
                .build();

        notification.addNotificationRecord(NotificationStatus.SUCCESS);
        return notification;
    }

    public void addNotificationRecord(NotificationStatus status) {
        NotificationRecord record = NotificationRecord.create(this, status);
        this.records.add(record);
    }

}