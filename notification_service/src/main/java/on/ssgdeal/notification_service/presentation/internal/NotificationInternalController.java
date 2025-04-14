package on.ssgdeal.notification_service.presentation.internal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.notification_service.application.service.NotificationServiceImpl;
import on.ssgdeal.notification_service.presentation.internal.dto.CreateNotificationRequest;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationResponseDto;
import on.ssgdeal.notification_service.presentation.internal.dto.mapper.NotificationPresentationMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "NotificationInternalController")
@RestController
@RequestMapping("/internal/v1/slack")
@RequiredArgsConstructor
public class NotificationInternalController {

    private final PassportUtil passportUtil;
    private final NotificationPresentationMapper notificationMapper;
    private final NotificationServiceImpl notificationServiceImpl;

    @PostMapping("/order/complete")
    public ResponseEntity<CommonResponse<CreateNotificationResponseDto>> createNotification(
            @Valid @RequestBody final CreateNotificationRequest request
//            HttpServletRequest servletRequest
    ) {
//        Passport passport = passportUtil.getPassportBy(servletRequest);
//        final var requestDto = notificationMapper.toNotificationRequestDto(request, passport.getSlackEmail());
        final var requestDto = notificationMapper.toNotificationRequestDto(request, "hyunj2034@naver.com");
        log.info("주문 완료 슬랙 메시지 요청 : {}", request);
        final var responseDto = notificationServiceImpl.sendSlackNotification(requestDto);
        return ResponseEntity.ok().body(CommonResponse.success(responseDto));
    }
}
