package on.ssgdeal.user_service.infrastructure.persistence.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.user_service.application.dto.user.SearchUserRequestDto;
import on.ssgdeal.user_service.domain.entity.User;
import on.ssgdeal.user_service.domain.repository.UserRepository;
import on.ssgdeal.user_service.domain.vo.SlackEmail;
import on.ssgdeal.user_service.infrastructure.persistence.jpa.UserJpaRepository;
import on.ssgdeal.user_service.infrastructure.persistence.jpa.querydsl.UserQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserQueryRepository userQueryRepository;


    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userJpaRepository.delete(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public Page<User> searchUser(SearchUserRequestDto requestDto) {
        return userQueryRepository.searchUser(requestDto);
    }

    @Override
    public Boolean existsBySlackEmail(SlackEmail SlackEmail) {
        return userJpaRepository.existsBySlackEmail(SlackEmail);
    }
}
