package on.ssgdeal.order_service.domain.entity.dtos;

import java.time.LocalDateTime;
import on.ssgdeal.order_service.domain.enums.PaymentMethod;
import on.ssgdeal.order_service.domain.enums.PaymentType;

public record UpdateTotalOrderSuccessDto(Long totalOrderId,
                                         Long paymentId,
                                         PaymentType paymentType,
                                         PaymentMethod paymentMethod,
                                         Long paymentAmount,
                                         LocalDateTime paymentDate,
                                         String paymentKey) {

}
