package on.ssgdeal.order_service.infrastructure.client.slack.feign.dtos;

import java.time.LocalDate;
import on.ssgdeal.order_service.application.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;
import on.ssgdeal.order_service.domain.enums.TotalOrderStatus;
import on.ssgdeal.order_service.infrastructure.client.slack.dtos.TotalOrderCompleteSendInfoDto;

public record OrderCompleteSendSlackRequestDto(Long totalOrderId,
                                               String slackEmail,
                                               String ordererName,
                                               LocalDate orderAt,
                                               Long paymentPrice,
                                               TotalOrderStatus orderStatus) {

    public static OrderCompleteSendSlackRequestDto from(
        UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto,
        TotalOrderCompleteSendInfoDto totalOrderCompleteSendInfoDto,
        LoginUserInfoDto loginUserInfoDto) {
        return new OrderCompleteSendSlackRequestDto(
            updateTotalOrderSuccessDto.totalOrderId(),
            loginUserInfoDto.slackEmail(),
            loginUserInfoDto.nickname(),
            totalOrderCompleteSendInfoDto.orderAt(),
            totalOrderCompleteSendInfoDto.paymentPrice(),
            TotalOrderStatus.PAID);
    }
}
