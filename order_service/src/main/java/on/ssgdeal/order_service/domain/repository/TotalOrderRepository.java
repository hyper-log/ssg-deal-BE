package on.ssgdeal.order_service.domain.repository;

import java.util.Optional;
import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.entity.dtos.GetTotalOrdersUserInfoDto;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TotalOrderRepository {

    TotalOrder save(TotalOrder totalOrder);

    Optional<TotalOrder> findById(Long id);

    void paymentSuccess(TotalOrder totalOrder,
        UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto);

    Page<TotalOrder> getTotalOrderList(
        GetTotalOrdersUserInfoDto getTotalOrdersUserInfoDto, Pageable pageable);
}
