package on.ssgdeal.user_service.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.user_service.application.dto.user.CreateUserRequestDto;
import on.ssgdeal.user_service.domain.vo.SlackEmail;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("is_deleted = false")
@Table(name = "\"user\"")
@SQLDelete(sql = "UPDATE user SET is_deleted = true WHERE id = ?")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nickname;
    private SlackEmail slackEmail;

    public static User create(CreateUserRequestDto dto) {
        return User.builder()
            .nickname(dto.nickname())
            .slackEmail(SlackEmail.valueOf(dto.slackEmail()))
            .build();
    }

    public String getSlackEmail() {
        return slackEmail.getEmail();
    }

    public void updateNickname(String nickname) {
        if (!this.nickname.equals(nickname) && nickname != null) {
            this.nickname = nickname;
        }
    }

    public void updateSlackEmail(SlackEmail slackEmail) {
        if (!this.slackEmail.equals(slackEmail) && slackEmail.toString() != null) {
            this.slackEmail = slackEmail;
        }
    }

}
