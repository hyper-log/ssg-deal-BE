package on.ssgdeal.payment_service.infrastructure.client.order;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.payment_service.application.service.OrderService;
import on.ssgdeal.payment_service.infrastructure.client.order.feign.OrderServiceFeignClient;
import on.ssgdeal.payment_service.infrastructure.client.order.feign.dto.GetValidTotalOrderId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceFeignClientImpl implements OrderService {

    private final OrderServiceFeignClient orderServiceFeignClient;

    @Override
    public GetValidTotalOrderId getValidTotalOrderId(Long totalOrderId) {
        var getValidTotalOrderDto = orderServiceFeignClient.getValidTotalOrderId(totalOrderId);
        return getValidTotalOrderDto.data();
    }
}
