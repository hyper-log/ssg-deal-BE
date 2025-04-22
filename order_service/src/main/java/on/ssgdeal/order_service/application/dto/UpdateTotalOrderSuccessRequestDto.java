package on.ssgdeal.order_service.application.dto;

import java.time.LocalDateTime;
import on.ssgdeal.order_service.domain.enums.PaymentMethod;
import on.ssgdeal.order_service.domain.enums.PaymentType;

public record UpdateTotalOrderSuccessRequestDto(Long totalOrderId,
                                                Long paymentId,
                                                PaymentType paymentType,
                                                PaymentMethod paymentMethod,
                                                Long paymentAmount,
                                                LocalDateTime paymentDate,
                                                String paymentKey) {

}
