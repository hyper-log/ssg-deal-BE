package on.ssgdeal.user_service.domain.repository;

import java.util.Optional;
import on.ssgdeal.user_service.application.dto.user.SearchUserRequestDto;
import on.ssgdeal.user_service.domain.entity.User;
import on.ssgdeal.user_service.domain.vo.SlackEmail;
import org.springframework.data.domain.Page;

public interface UserRepository {

    User save(User user);

    void delete(User user);

    Optional<User> findById(Long id);

    Page<User> searchUser(SearchUserRequestDto requestDto);

    Boolean existsBySlackEmail(SlackEmail SlackEmail);
}
