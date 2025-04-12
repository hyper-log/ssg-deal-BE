package on.ssgdeal.order_service.infrastructure.persistence.jpa.querydsl;

import java.util.Optional;
import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.entity.dtos.GetTotalOrderDetailDto;
import on.ssgdeal.order_service.domain.entity.dtos.GetTotalOrdersUserInfoDto;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TotalOrderQueryRepository {

    void paymentSuccess(TotalOrder totalOrder,
        UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto);

    Page<TotalOrder> getTotalOrderList(
        GetTotalOrdersUserInfoDto getTotalOrdersUserInfoDto, Pageable pageable);

    TotalOrder getTotalOrderDetail(GetTotalOrderDetailDto getTotalOrderDetailDto);

    void cancelUpdateStatusTotalOrder(TotalOrder totalOrder);

    Optional<TotalOrder> findTotalOrderForCancel(Long totalOrderId);

    Optional<TotalOrder> findTotalOrderForFail(Long totalOrderId);
}
