package on.ssgdeal.order_service.infrastructure.messaging.dtos;

import java.time.LocalDate;
import on.ssgdeal.common.messaging.domain.entity.EventPayload;
import on.ssgdeal.order_service.application.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;
import on.ssgdeal.order_service.domain.enums.TotalOrderStatus;
import on.ssgdeal.order_service.infrastructure.client.slack.dtos.TotalOrderCompleteSendInfoDto;

public record OrderSuccessNotificationEvent(Long totalOrderId,
                                            String slackEmail,
                                            String ordererName,
                                            LocalDate orderAt,
                                            Long paymentPrice,
                                            TotalOrderStatus orderStatus) implements EventPayload {

    public static OrderSuccessNotificationEvent from(
        UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto,
        TotalOrderCompleteSendInfoDto totalOrderCompleteSendInfoDto,
        LoginUserInfoDto loginUserInfoDto) {
        return new OrderSuccessNotificationEvent(
            updateTotalOrderSuccessDto.totalOrderId(),
            loginUserInfoDto.slackEmail(),
            loginUserInfoDto.nickname(),
            totalOrderCompleteSendInfoDto.orderAt(),
            totalOrderCompleteSendInfoDto.paymentPrice(),
            TotalOrderStatus.PAID);
    }
}
