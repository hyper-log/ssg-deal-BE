package on.ssgdeal.user_service.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import on.ssgdeal.common.auth.enums.AuthRole;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.user_service.application.dto.destination.CreateMyDestinationRequestDto;
import on.ssgdeal.user_service.application.dto.destination.UpdateMyDestinationRequestDto;
import on.ssgdeal.user_service.domain.entity.Destination;
import on.ssgdeal.user_service.domain.entity.Destination.CreateDestinationDto;
import on.ssgdeal.user_service.domain.repository.DestinationRepository;
import on.ssgdeal.user_service.domain.vo.Address;
import on.ssgdeal.user_service.exception.destination.DestinationException;
import on.ssgdeal.user_service.presentation.external.dto.destination.CreateMyDestinationResponse;
import on.ssgdeal.user_service.presentation.external.dto.destination.FindAllMyDestinationsResponse;
import on.ssgdeal.user_service.presentation.external.dto.destination.FindMyDestinationResponse;
import on.ssgdeal.user_service.presentation.external.dto.destination.UpdateMyDestinationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class DestinationServiceImplTest {

    @MockitoBean
    PassportUtil passportUtil;

    @Autowired
    private DestinationService destinationService;
    @Autowired
    private DestinationRepository destinationRepository;

    private Passport createConsumerPassport() {
        return new Passport(1L, "consumer", AuthRole.CONSUMER, "consumer", "consumer@ssgdeal.com");
    }

    private Passport createCompanyManagerPassport() {
        return new Passport(2L, "comManager", AuthRole.COMPANY_MANAGER, "comManager",
            "comManager@ssgdeal.com");
    }


    @Nested
    @DisplayName("Describe: findAllMy 메서드는")
    class FindAllMyTest {

        @Nested
        @DisplayName("Context: 사용자의 목적지가 존재할 때")
        class FindAllMySuccessTest {

            @Test
            @DisplayName("It: 사용자의 목적지 목록을 반환한다.")
            void findAllMy_success() {
                // given
                HttpServletRequest request = mock(HttpServletRequest.class);
                Passport passport = createConsumerPassport();
                when(passportUtil.getPassportBy(request)).thenReturn(passport);

                Destination destination1 = Destination.builder()
                    .address(Address.valueOf("address1"))
                    .name("destinationName1")
                    .userId(passport.getUserId())
                    .build();

                Destination destination2 = Destination.builder()
                    .address(Address.valueOf("address2"))
                    .name("destinationName2")
                    .userId(passport.getUserId())
                    .build();

                Destination savedDestination1 = destinationRepository.save(destination1);
                Destination savedDestination2 = destinationRepository.save(destination2);

                // when
                FindAllMyDestinationsResponse response = destinationService.findAllMy(request);

                // then
                assertThat(response).isNotNull();
                assertThat(response.destinations().size()).isEqualTo(2);
                assertThat((response.destinations().get(0)).destinationId())
                    .isEqualTo(savedDestination1.getId());
                assertThat((response.destinations().get(1)).destinationId())
                    .isEqualTo(savedDestination2.getId());
            }
        }

        @Nested
        @DisplayName("Context: 사용자의 목적지가 존재하지 않을 때")
        class FindAllMyEmptyTest {

            @Test
            @DisplayName("It: 목적지 미존재 오류(DestinationException)를 반환한다.")
            void findAllMy_empty_failure() {
                // given
                HttpServletRequest request = mock(HttpServletRequest.class);
                Passport passport = createConsumerPassport();
                when(passportUtil.getPassportBy(request)).thenReturn(passport);

                // when & then
                assertThatThrownBy(() -> destinationService.findAllMy(request))
                    .isInstanceOf(DestinationException.DestinationNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("Describe: createMy 메서드는")
    class CreateMyTest {

        @Nested
        @DisplayName("Context: 올바른 값이 주어졌을 때")
        class CreateMySuccessTest {

            @Test
            @DisplayName("It: 데이터를 저장하여 목적지 ID를 반환한다.")
            void createMy_success() {
                // given
                HttpServletRequest request = mock(HttpServletRequest.class);
                Passport passport = createConsumerPassport();
                when(passportUtil.getPassportBy(request)).thenReturn(passport);

                CreateMyDestinationRequestDto requestDto = new CreateMyDestinationRequestDto(
                    "destinationName", "destinationAddress");
                CreateDestinationDto createDestinationDto = requestDto.toCreateDestinationDto();

                Destination destination = Destination.create(passport, createDestinationDto);

                Destination savedDestination = destinationRepository.save(destination);

                // when
                CreateMyDestinationResponse response = destinationService.createMy(request,
                    requestDto);

                // then
                assertThat(response).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("Describe: updateMy 메서드는")
    class UpdateMyTest {

        @Nested
        @DisplayName("Context: 이미 존재하는 본인 목적지에 대해 올바른 값이 들어왔을 때")
        class UpdateMySuccessTest {

            @Test
            @DisplayName("It: 목적지를 수정하여 업데이트된 정보를 반환한다.")
            void updateMy_success() {
                // given
                HttpServletRequest request = mock(HttpServletRequest.class);
                Passport passport = createConsumerPassport();
                when(passportUtil.getPassportBy(request)).thenReturn(passport);

                Destination destination = Destination.builder()
                    .address(Address.valueOf("address"))
                    .name("destinationName")
                    .userId(passport.getUserId())
                    .build();

                Destination savedDestination = destinationRepository.save(destination);
                Long destinationId = savedDestination.getId();

                UpdateMyDestinationRequestDto updateDto = new UpdateMyDestinationRequestDto(
                    destinationId,
                    "newAddress",
                    "newName"
                );

                destination.update(updateDto.toUpdateDestinationDto());

                // when
                UpdateMyDestinationResponse response = destinationService.updateMy(request,
                    updateDto);

                // then
                assertThat(response).isNotNull();
                assertThat(response.destinationName()).isEqualTo("newName");
                assertThat(response.address()).isEqualTo("newAddress");
            }
        }
    }

    @Nested
    @DisplayName("Describe: deleteMy 메서드는")
    class DeleteMyTest {

        @Nested
        @DisplayName("Context: 존재하는 목적지 ID가 입력됐을 때")
        class DeleteMySuccessTest {

            @Test
            @DisplayName("It: 해당 목적지를 삭제한다.")
            void deleteMy_success() {
                // given
                HttpServletRequest request = mock(HttpServletRequest.class);
                Passport passport = createConsumerPassport();
                when(passportUtil.getPassportBy(request)).thenReturn(passport);

                Destination destination = Destination.builder()
                    .address(Address.valueOf("address"))
                    .name("destinationName")
                    .userId(passport.getUserId())
                    .build();

                Destination savedDestination = destinationRepository.save(destination);
                Long destinationId = savedDestination.getId();

                // when
                destinationService.deleteMy(request, destinationId);

                // then
                assertThat(destinationRepository.findById(destinationId).isPresent()).isFalse();
            }
        }

        @Nested
        @DisplayName("Context: 존재하지 않는 목적지 ID가 입력됐을 때")
        class DeleteMyFailureTest {

            @Test
            @DisplayName("It: DestinationNotFoundException을 반환한다.")
            void deleteMy_failure() {
                // given
                HttpServletRequest request = mock(HttpServletRequest.class);
                Passport passport = createConsumerPassport();
                when(passportUtil.getPassportBy(request)).thenReturn(passport);

                Long invalidDestinationId = 999L;

                Destination destination = Destination.builder()
                    .address(Address.valueOf("address"))
                    .name("destinationName")
                    .userId(passport.getUserId())
                    .build();

                destinationRepository.save(destination);

                // when & then
                assertThatThrownBy(() -> destinationService.deleteMy(request, invalidDestinationId))
                    .isInstanceOf(DestinationException.DestinationNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("Describe: findMy 메서드는")
    class FindMyTest {

        @Nested
        @DisplayName("Context: 존재하는 목적지 ID가 입력됐을 때")
        class FindMySuccessTest {

            @Test
            @DisplayName("It: 목적지 정보를 반환한다.")
            void findMy_success() {
                // given
                HttpServletRequest request = mock(HttpServletRequest.class);
                Passport passport = createConsumerPassport();
                when(passportUtil.getPassportBy(request)).thenReturn(passport);

                Destination destination = Destination.builder()
                    .address(Address.valueOf("address"))
                    .name("destinationName")
                    .userId(passport.getUserId())
                    .build();

                Destination savedDestination = destinationRepository.save(destination);
                Long destinationId = savedDestination.getId();

                // when
                FindMyDestinationResponse response = destinationService.findMy(request,
                    destinationId);

                // then
                assertThat(response).isNotNull();
                assertThat(response.destinationId()).isEqualTo(destinationId);
            }
        }

        @Nested
        @DisplayName("Context: 존재하지 않는 목적지 ID가 입력됐을 때")
        class FindMyFailureTest {

            @Test
            @DisplayName("It: DestinationNotFoundException을 반환한다.")
            void findMy_failure() {
                // given
                HttpServletRequest request = mock(HttpServletRequest.class);
                Passport passport = createConsumerPassport();
                when(passportUtil.getPassportBy(request)).thenReturn(passport);

                Long invalidDestinationId = 999L;

                Destination destination = Destination.builder()
                    .address(Address.valueOf("address"))
                    .name("destinationName")
                    .userId(passport.getUserId())
                    .build();

                destinationRepository.save(destination);

                // when & then
                assertThatThrownBy(() -> destinationService.findMy(request, invalidDestinationId))
                    .isInstanceOf(DestinationException.DestinationNotFoundException.class);
            }
        }
    }
}