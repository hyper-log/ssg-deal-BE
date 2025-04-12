package on.ssgdeal.payment_service.application.service;

import on.ssgdeal.payment_service.infrastructure.client.order.feign.dto.GetValidTotalOrderId;

public interface OrderService {

    GetValidTotalOrderId getValidTotalOrderId(Long totalOrderId);
}
