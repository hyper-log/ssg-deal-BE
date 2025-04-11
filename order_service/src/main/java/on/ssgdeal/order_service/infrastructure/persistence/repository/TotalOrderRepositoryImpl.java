package on.ssgdeal.order_service.infrastructure.persistence.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.entity.dtos.GetTotalOrdersUserInfoDto;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;
import on.ssgdeal.order_service.domain.repository.TotalOrderRepository;
import on.ssgdeal.order_service.infrastructure.persistence.jpa.TotalOrderJpaRepository;
import on.ssgdeal.order_service.infrastructure.persistence.jpa.querydsl.TotalOrderQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TotalOrderRepositoryImpl implements TotalOrderRepository {

    private final TotalOrderJpaRepository jpaRepository;
    private final TotalOrderQueryRepository queryRepository;

    @Override
    public TotalOrder save(TotalOrder totalOrder) {
        return jpaRepository.save(totalOrder);
    }

    @Override
    public Optional<TotalOrder> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public void paymentSuccess(TotalOrder totalOrder,
        UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto) {
        queryRepository.paymentSuccess(totalOrder, updateTotalOrderSuccessDto);
    }

    @Override
    public Page<TotalOrder> getTotalOrderList(
        GetTotalOrdersUserInfoDto getTotalOrdersUserInfoDto, Pageable pageable) {

        return queryRepository.getTotalOrderList(getTotalOrdersUserInfoDto, pageable);
    }

}
