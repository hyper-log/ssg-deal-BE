package on.ssgdeal.order_service.infrastructure.client.slack.dtos;

import java.time.LocalDate;

public record TotalOrderCompleteSendInfoDto(LocalDate orderAt, Long paymentPrice) {

    public static TotalOrderCompleteSendInfoDto from(LocalDate orderAt, Long paymentPrice) {
        return new TotalOrderCompleteSendInfoDto(orderAt, paymentPrice);
    }

}
