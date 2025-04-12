package on.ssgdeal.user_service.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import on.ssgdeal.common.auth.enums.AuthRole;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.user_service.application.dto.user.UpdateUserRequestDto;
import on.ssgdeal.user_service.domain.entity.User;
import on.ssgdeal.user_service.domain.repository.UserRepository;
import on.ssgdeal.user_service.domain.vo.SlackEmail;
import on.ssgdeal.user_service.exception.UserException.UserNotFoundException;
import on.ssgdeal.user_service.exception.UserException.UserSlackEmailAlreadyExistsException;
import on.ssgdeal.user_service.presentation.external.dto.user.CreateUserRequest;
import on.ssgdeal.user_service.presentation.external.dto.user.UpdateUserAdminRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @MockitoBean
    PassportUtil passportUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

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
        @DisplayName("Context: 이미 존재하는 슬랙 이메일이 존재할 때")
        class createFailUserAlreadyExistsTest {

            @Test
            @DisplayName("It: UserSlackEmailAlreadyExist 에러를 반환한다.")
            void createUser_failed_alreadyExists() {
                // given
                String email = "test@ssgdeal.com";

                String nickname1 = "test";
                var request1 = new CreateUserRequest(nickname1, email);
                var dto1 = request1.toDto();

                String nickname2 = "test2";
                var request2 = new CreateUserRequest(nickname2, email);
                var dto2 = request2.toDto();

                // when
                userService.createUser(dto1);

                // when & then
                assertThatThrownBy(() -> userService.createUser(dto2))
                    .isInstanceOf(UserSlackEmailAlreadyExistsException.class);

            }

        }

    }

    @Nested
    @DisplayName("Describe: findUserById 메서드는")
    class findUserByIdTest {

        @Nested
        @DisplayName("Context: 존재하는 Id 값이 입력됐을 때")
        class findUserByIdSuccessTest {

            @Test
            @DisplayName("It: UserId와 nickname, slackEmail을 반환한다.")
            void findUserById_success() {
                // given
                String nickname = "test";
                String email = "test@ssgdeal.com";
                var request = new CreateUserRequest(nickname, email);
                var dto = request.toDto();

                var savedUserResponse = userService.createUser(dto);
                Long userId = savedUserResponse.userId();

                // when
                var result = userService.findUserById(userId);

                // then
                assertThat(result).isNotNull();
                assertThat(result.nickname()).isEqualTo(nickname);
                assertThat(result.slackEmail()).isEqualTo(email);
                assertThat(result.userId()).isEqualTo(userId);

            }
        }

        @Nested
        @DisplayName("Context: 존재하지 않는 Id 값이 입력됐을 때")
        class findUserByIdFailTest {

            @Test
            @DisplayName("It: UserNotFound 오류를 반환한다.")
            void findUserById_failed() {
                // given
                Long userId = 0L;

                // when & then
                assertThatThrownBy(() -> userService.findUserById(userId))
                    .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("Describe: withdrawUserByUserId 메서드는")
    class WithdrawUserByUserIdTest {

        @Nested
        @DisplayName("Context: 존재하는 Id 값이 입력됐을 때")
        class WithdrawUserByUserIdSuccessTest {

            @Test
            @DisplayName("It: 해당 유저를 삭제한다.")
            void withdrawUserByUserId_success() {
                // given
                String nickname = "test";
                String email = "test@ssgdeal.com";
                var request = new CreateUserRequest(nickname, email);
                var dto = request.toDto();

                var savedUser = userService.createUser(dto);
                Long userId = savedUser.userId();

                // when
                userService.withdrawUserByUserId(userId);

                // then
                assertThatThrownBy(() -> userService.findUserById(userId))
                    .isInstanceOf(UserNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("Context: 존재하지 않는 Id 값이 입력됐을 때")
        class WithdrawUserByUserIdFailTest {

            @Test
            @DisplayName("It: UserNotFound 오류를 반환한다.")
            void withdrawUserByUserId_failed() {
                // given
                Long invalidUserId = 0L;

                // when & then
                assertThatThrownBy(() -> userService.withdrawUserByUserId(invalidUserId))
                    .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("Describe: getSlackEmailById 메서드는")
    class GetSlackEmailByIdTest {

        @Nested
        @DisplayName("Context: 올바른 ID 값이 들어왔을 때")
        class GetSlackEmailByIdSuccessTest {

            @Test
            @DisplayName("It: 존재하는 ID로 SlackEmail을 반환한다.")
            void getSlackEmailById_success() {
                // given
                String nickname = "test";
                String email = "test@ssgdeal.com";
                var dto = new CreateUserRequest(nickname, email).toDto();
                var saved = userService.createUser(dto);

                // when
                var result = userService.getSlackEmailById(saved.userId());

                // then
                assertThat(result).isNotNull();
                assertThat(result.slackEmail()).isEqualTo(email);
            }
        }

        @Nested
        @DisplayName("Context: 존재하지 않는 ID 값이 들어왔을 때")
        class GetSlackEmailByIdFailTest {

            @Test
            @DisplayName("It: UserNotFoundException이 발생한다.")
            void getSlackEmailById_fail() {
                assertThatThrownBy(() -> userService.getSlackEmailById(9999L))
                    .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("Describe: updateUserAdmin 메서드는")
    class UpdateUserAdminTest {

        @Nested
        @DisplayName("Context: 이미 존재하는 슬랙 이메일로 수정할 때")
        class updateUserAdminFailTest {

            @Test
            @DisplayName("It: SlackEmailAlreadyExists 예외가 발생한다.")
            void updateUserAdmin_emailDuplicate_fail() {
                // given
                userService.createUser(new CreateUserRequest("user1", "dup@ssg.com").toDto());
                var user2 = userService.createUser(
                    new CreateUserRequest("user2", "user2@ssg.com").toDto());

                var request = new UpdateUserAdminRequest("newNick", "dup@ssg.com");
                var dto = request.toDto(user2.userId());

                // when & then
                assertThatThrownBy(() -> userService.updateUserAdmin(dto))
                    .isInstanceOf(UserSlackEmailAlreadyExistsException.class);
            }
        }

        @Nested
        @DisplayName("Context: 존재하는 유저에 대해 중복되지 않은 데이터로 변경할 때")
        class UpdateUserAdminSuccessTest {

            @Test
            @DisplayName("It: 관리자가 유저 정보를 수정할 수 있다.")
            void updateUserAdmin_success() {
                // given
                var user = userService.createUser(
                    new CreateUserRequest("old", "old@ssg.com").toDto());
                var request = new UpdateUserAdminRequest("new", "new@ssg.com");
                var dto = request.toDto(user.userId());

                // when
                var result = userService.updateUserAdmin(dto);

                // then
                assertThat(result.nickname()).isEqualTo("new");
                assertThat(result.slackEmail()).isEqualTo("new@ssg.com");
            }
        }
    }

    @Nested
    @DisplayName("Describe: updateUser 메서드는")
    class UpdateUserTest {

        @Nested
        @DisplayName("Context: 수정 가능한 값이 들어왔을 때")
        class updateUserSuccessTest {

            @Test
            @DisplayName("It: 로그인한 사용자가 자신의 정보를 수정할 수 있다.")
            void updateUser_success() {
                // given
                var saved = userService.createUser(
                    new CreateUserRequest("old", "old@ssg.com").toDto());
                var updateDto = new UpdateUserRequestDto("newNick", new SlackEmail("new@ssg.com"));

                var mockRequest = new MockHttpServletRequest();
                when(passportUtil
                    .getPassportBy((HttpServletRequest) ArgumentMatchers.any()))
                    .thenReturn(new Passport(
                        saved.userId(),
                        "oldId",
                        AuthRole.CONSUMER,
                        "old",
                        "old@ssg.com")
                    );

                // when
                var result = userService.updateUser(updateDto, mockRequest);

                // then
                assertThat(result.nickname()).isEqualTo("newNick");
                assertThat(result.slackEmail()).isEqualTo("new@ssg.com");
            }
        }
    }


}