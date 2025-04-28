package on.ssgdeal.order_service.infrastructure.messaging.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import on.ssgdeal.common.messaging.core.EventPayload;
import on.ssgdeal.order_service.application.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;
import on.ssgdeal.order_service.domain.enums.TotalOrderStatus;
import on.ssgdeal.order_service.infrastructure.client.slack.dtos.TotalOrderCompleteSendInfoDto;

public record OrderSuccessNotificationEvent(
    Long totalOrderId,
    String slackEmail,
    String ordererName,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate orderAt,
    Long paymentPrice,
    String orderStatus
) implements EventPayload {

    public static OrderSuccessNotificationEvent from(
        UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto,
        TotalOrderCompleteSendInfoDto totalOrderCompleteSendInfoDto,
        LoginUserInfoDto loginUserInfoDto
    ) {
        return new OrderSuccessNotificationEvent(
            updateTotalOrderSuccessDto.totalOrderId(),
            loginUserInfoDto.slackEmail(),
            loginUserInfoDto.nickname(),
            totalOrderCompleteSendInfoDto.orderAt(),
            totalOrderCompleteSendInfoDto.paymentPrice(),
            TotalOrderStatus.PAID.getDescription()
        );
    }
}
