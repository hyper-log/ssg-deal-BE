package on.ssgdeal.notification_service.presentation.internal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreateNotificationRequest(
        @NotBlank @Email String slackEmail,
        @NotNull Long totalOrderId,
        @NotBlank String ordererName,
        @NotNull LocalDate orderAt,
        @NotNull @PositiveOrZero Long paymentPrice,
        @NotBlank String orderStatus
) {
}