package on.ssgdeal.user_service.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.user_service.application.dto.user.CreateUserRequestDto;
import on.ssgdeal.user_service.application.dto.user.SearchUserRequestDto;
import on.ssgdeal.user_service.application.dto.user.UpdateUserAdminRequestDto;
import on.ssgdeal.user_service.application.dto.user.UpdateUserRequestDto;
import on.ssgdeal.user_service.domain.entity.User;
import on.ssgdeal.user_service.domain.repository.UserRepository;
import on.ssgdeal.user_service.domain.util.AuditFieldUpdater;
import on.ssgdeal.user_service.domain.vo.SlackEmail;
import on.ssgdeal.user_service.exception.UserException;
import on.ssgdeal.user_service.exception.UserException.UserNotFoundException;
import on.ssgdeal.user_service.presentation.external.dto.user.CreateUserResponse;
import on.ssgdeal.user_service.presentation.external.dto.user.FindSlackEmailByIdResponse;
import on.ssgdeal.user_service.presentation.external.dto.user.SearchUserResponse;
import on.ssgdeal.user_service.presentation.external.dto.user.UpdateUserAdminResponse;
import on.ssgdeal.user_service.presentation.external.dto.user.UpdateUserResponse;
import on.ssgdeal.user_service.presentation.internal.dto.FindByIdUserResponse;
import on.ssgdeal.user_service.presentation.internal.dto.FindMyUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "UserServiceImpl")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PassportUtil passportUtil;

    private static void dtoNullValidate(CreateUserRequestDto dto)
        throws UserException.UserNicknameIsNullException {
        if (dto.nickname() == null || dto.nickname().isEmpty()) {
            throw new UserException.UserNicknameIsNullException();
        }

        if (dto.slackEmail() == null || dto.slackEmail().isEmpty()) {
            throw new UserException.UserSlackEmailIsNullException();
        }
    }

    @Override
    @Transactional
    public CreateUserResponse createUser(
        CreateUserRequestDto dto
    ) {

        dtoNullValidate(dto);

        if (userRepository.existsBySlackEmail(SlackEmail.valueOf(dto.slackEmail()))) {
            throw new UserException.UserSlackEmailAlreadyExistsException();
        }

        User user = User.create(dto);

        User savedUser = userRepository.save(user);
        AuditFieldUpdater.updateAuditFields(savedUser, savedUser.getId());

        return CreateUserResponse.from(savedUser);
    }

    @Override
    public FindByIdUserResponse findUserById(
        Long id
    ) {
        User user = findByIdOrElseThrow(id);
        return FindByIdUserResponse.from(user);
    }

    @Override
    public FindByIdUserResponse findUserByIdInternal(Long id) {
        User user = findByIdOrElseThrow(id);
        return FindByIdUserResponse.from(user);
    }

    @Override
    public void withdrawUserByUserId(Long id) {
        User user = findByIdOrElseThrow(id);
        userRepository.delete(user);
    }

    @Override
    public FindMyUserResponse findMyUser(HttpServletRequest request) {
        Passport passport = passportUtil.getPassportBy(request);
        Long id = passport.getUserId();

        User user = findByIdOrElseThrow(id);
        return FindMyUserResponse.from(user);
    }

    @Override
    public PageDto<SearchUserResponse> searchUser(SearchUserRequestDto dto) {
        Page<User> users = userRepository.searchUser(dto);
        Page<SearchUserResponse> responsePage = users.map(SearchUserResponse::from);
        return PageDto.from(responsePage);
    }

    @Override
    @Transactional
    public UpdateUserResponse updateUser(UpdateUserRequestDto dto, HttpServletRequest request) {
        Passport passport = passportUtil.getPassportBy(request);

        if (userRepository.existsBySlackEmail(dto.slackEmail())) {
            throw new UserException.UserSlackEmailAlreadyExistsException();
        }

        Long id = passport.getUserId();

        User user = findByIdOrElseThrow(id);

        user.updateNickname(dto.nickname());
        user.updateSlackEmail(dto.slackEmail());

        return UpdateUserResponse.from(user);
    }

    @Override
    @Transactional
    public UpdateUserAdminResponse updateUserAdmin(
        UpdateUserAdminRequestDto dto
    ) {
        if (userRepository.existsBySlackEmail(dto.slackEmail())) {
            throw new UserException.UserSlackEmailAlreadyExistsException();
        }

        User user = findByIdOrElseThrow(dto.userId());

        user.updateNickname(dto.nickname());
        user.updateSlackEmail(dto.slackEmail());

        return UpdateUserAdminResponse.from(user);
    }


    @Override
    public FindSlackEmailByIdResponse getSlackEmailById(
        Long id
    ) {
        User user = findByIdOrElseThrow(id);
        return FindSlackEmailByIdResponse.from(user);
    }

    private User findByIdOrElseThrow(Long id) {
        return userRepository.findById(id).orElseThrow(
            UserNotFoundException::new
        );
    }


}
