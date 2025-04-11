package on.ssgdeal.user_service.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import on.ssgdeal.user_service.application.dto.CreateUserDto;
import on.ssgdeal.user_service.domain.entity.User;
import on.ssgdeal.user_service.domain.repository.UserRepository;
import on.ssgdeal.user_service.domain.vo.SlackEmail;
import on.ssgdeal.user_service.presentation.external.dto.CreateUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findUserById() {
        CreateUserDto dto = new CreateUserDto("test2", new SlackEmail("test2@slack.com"));


    }

    @Test
    void findUserByIdInternal() {
    }

    @Test
    void withdrawUserByUserId() {
    }

    @Test
    void findMyUser() {
    }

    @Test
    void searchUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void updateUserAdmin() {
    }

    @Test
    void getSlackEmailById() {
    }

    @Nested
    @DisplayName("Describe: createUser 메서드는")
    class createUserTest {

        @Nested
        @DisplayName("Context: 필수 입력과 값 검증이 성공했을 때")
        class createSuccessTest {

            @Test
            @DisplayName("It: 데이터를 저장하여 UserId와 nickname, slackEmail을 반환한다.")
            void createUser_success() {
                // given
                String nickname = "test";
                String email = "test@ssgdeal.com";
                var request = new CreateUserRequest(nickname, email);
                var dto = request.toDto();

                // when
                var result = userService.createUser(dto);

                // then
                assertThat(result).isNotNull();
                assertThat(result.userId()).isNotNull();
                assertThat(result.nickname()).isEqualTo(nickname);
                assertThat(result.slackEmail()).isEqualTo(email);

                User savedUser = userRepository.findById(result.userId()).orElseThrow(null);
                assertThat(savedUser.getId()).isEqualTo(result.userId());
            }

        }

        @Nested
        @DisplayName("Context: 이미 존재하는 회원이 존재할 때")
        class createFailUserAlreadyExistsTest {

            @Test
            @DisplayName("It: UserAlreadyExists 에러를 반환한다.")
            void createUser_failed_alreadyExists() {
                // given
                String nickname1 = "test";
                String email1 = "test@ssgdeal.com";
                var request1 = new CreateUserRequest(nickname1, email1);
                var dto1 = request1.toDto();
                var dto2 = request1.toDto();


            }

        }

    }
}