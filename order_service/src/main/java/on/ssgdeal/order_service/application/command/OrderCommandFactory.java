package on.ssgdeal.order_service.application.command;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.command.CommandFactory;
import on.ssgdeal.order_service.application.command.support.OrderCommandSupport;
import on.ssgdeal.order_service.application.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.domain.repository.TotalOrderRepository;
import on.ssgdeal.order_service.util.OrderNumberGenerator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCommandFactory implements CommandFactory {

    private final TotalOrderRepository totalOrderRepository;
    private final OrderNumberGenerator orderNumberGenerator;
    private final OrderCommandSupport support;

    public CreateOrderCommand createOrderCommand(
        CreateOrderRequestDto request,
        LoginUserInfoDto loginUserInfoDto
    ) {
        return new CreateOrderCommand(
            request,
            loginUserInfoDto,
            totalOrderRepository,
            orderNumberGenerator,
            support
        );
    }
}

