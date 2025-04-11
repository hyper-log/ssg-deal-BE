package on.ssgdeal.order_service.infrastructure.persistence.jpa.querydsl;

import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.entity.dtos.GetTotalOrdersUserInfoDto;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TotalOrderQueryRepository {

    void paymentSuccess(TotalOrder totalOrder,
        UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto);

    Page<TotalOrder> getTotalOrderList(
        GetTotalOrdersUserInfoDto getTotalOrdersUserInfoDto, Pageable pageable);
}
