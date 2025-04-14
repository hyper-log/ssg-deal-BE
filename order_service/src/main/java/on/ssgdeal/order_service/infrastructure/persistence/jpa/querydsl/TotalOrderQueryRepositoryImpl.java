package on.ssgdeal.order_service.infrastructure.persistence.jpa.querydsl;

import static on.ssgdeal.order_service.domain.entity.QOrder.order;
import static on.ssgdeal.order_service.domain.entity.QOrderProduct.orderProduct;
import static on.ssgdeal.order_service.domain.entity.QOrderer.orderer;
import static on.ssgdeal.order_service.domain.entity.QTotalOrder.totalOrder;
import static on.ssgdeal.order_service.domain.entity.QTotalOrderPayment.totalOrderPayment;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.pageable.enums.PageSortBy;
import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.entity.dtos.GetTotalOrderDetailDto;
import on.ssgdeal.order_service.domain.entity.dtos.GetTotalOrdersUserInfoDto;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;
import on.ssgdeal.order_service.domain.enums.OrderStatus;
import on.ssgdeal.order_service.domain.enums.PaymentStatus;
import on.ssgdeal.order_service.domain.enums.TotalOrderStatus;
import on.ssgdeal.order_service.exception.OrderException.OrderNotFoundTotalOrderException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j(topic = "TotalOrderQueryRepositoryImpl")
public class TotalOrderQueryRepositoryImpl implements TotalOrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    private static List<TotalOrderStatus> getTotalOrderStatuses() {
        List<TotalOrderStatus> excludedStatuses = List.of(
            TotalOrderStatus.FAILED,
            TotalOrderStatus.EXPIRED
        );

        return Arrays.stream(TotalOrderStatus.values())
            .filter(status -> !excludedStatuses.contains(status))
            .toList();
    }

    @Override
    public void paymentSuccess(TotalOrder requestTotalOrder,
        UpdateTotalOrderSuccessDto requestDto) {
        queryFactory.update(totalOrder)
            .set(totalOrder.status, TotalOrderStatus.PAID)
            .where(totalOrder.id.eq(requestTotalOrder.getId()))
            .execute();
        entityManager.flush();

        queryFactory.update(order)
            .set(order.status, OrderStatus.PAID)
            .where(order.totalOrder.eq(requestTotalOrder))
            .execute();
        entityManager.flush();

        queryFactory.update(totalOrderPayment)
            .set(totalOrderPayment.paymentId, requestDto.paymentId())
            .set(totalOrderPayment.paymentType, requestDto.paymentType())
            .set(totalOrderPayment.paymentMethod, requestDto.paymentMethod())
            .set(totalOrderPayment.paymentDate, Timestamp.valueOf(requestDto.paymentDate()))
            .set(totalOrderPayment.paymentAmount, requestDto.paymentAmount())
            .set(totalOrderPayment.paymentKey, requestDto.paymentKey())
            .set(totalOrderPayment.paymentStatus, PaymentStatus.COMPLETED)
            .where(totalOrderPayment.totalOrder.eq(requestTotalOrder))
            .execute();
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public Page<TotalOrder> getTotalOrderList(
        GetTotalOrdersUserInfoDto getTotalOrdersUserInfoDto, Pageable pageable) {
        BooleanBuilder totalOrderFilter = getTotalOrderFilter(getTotalOrdersUserInfoDto);
        List<TotalOrder> totalOrderList = getTotalOrder(getTotalOrdersUserInfoDto, pageable,
            totalOrderFilter);
        Long total = totalCount(getTotalOrdersUserInfoDto, totalOrderFilter);
        return new PageImpl<>(totalOrderList, pageable, total);
    }

    @Override
    public TotalOrder getTotalOrderDetail(GetTotalOrderDetailDto getTotalOrderDetailDto) {
        BooleanBuilder totalOrderDetailFilter = getTotalOrderDetailFilter(getTotalOrderDetailDto);

        return getTotalOrderDetailInfo(totalOrderDetailFilter);
    }

    @Override
    public void cancelUpdateStatusTotalOrder(TotalOrder requestTotalOrder) {
        queryFactory.update(totalOrder)
            .set(totalOrder.status, TotalOrderStatus.CANCELED)
            .where(totalOrder.id.eq(requestTotalOrder.getId()))
            .execute();
        entityManager.flush();

        queryFactory.update(order)
            .set(order.status, OrderStatus.CANCELED)
            .where(order.totalOrder.eq(requestTotalOrder))
            .execute();
        entityManager.flush();

        queryFactory.update(totalOrderPayment)
            .set(totalOrderPayment.paymentStatus, PaymentStatus.CANCELED)
            .where(totalOrderPayment.totalOrder.eq(requestTotalOrder))
            .execute();
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public TotalOrder findTotalOrderForCancel(Long totalOrderId) {
        List<TotalOrder> results = queryFactory
            .selectDistinct(totalOrder)
            .from(totalOrder)
            .join(totalOrder.orders, order).fetchJoin()
            .join(totalOrder.totalOrderPayments, totalOrderPayment)
            .where(
                totalOrder.id.eq(totalOrderId),
                order.status.ne(OrderStatus.CANCELED)
            )
            .fetch();

        if (results.isEmpty()) {
            throw new OrderNotFoundTotalOrderException();
        }

        return results.get(0);
    }

    @Override
    public TotalOrder findTotalOrderForFail(Long totalOrderId) {
        List<TotalOrder> results = queryFactory
            .selectDistinct(totalOrder)
            .join(totalOrder.orders, order).fetchJoin()
            .join(totalOrder.totalOrderPayments, totalOrderPayment)
            .where(
                totalOrder.id.eq(totalOrderId),
                totalOrder.status.eq(TotalOrderStatus.PENDING)
            )
            .fetch();

        if (results.isEmpty()) {
            throw new OrderNotFoundTotalOrderException();
        }

        return results.get(0);
    }

    @Override
    public TotalOrder findOrderForCancel(Long totalOrderId, Long orderId) {
        List<TotalOrder> results = queryFactory
            .selectDistinct(totalOrder)
            .from(totalOrder)
            .join(totalOrder.orders, order)
            .join(order.orderProducts, orderProduct)
            .where(
                totalOrder.id.eq(totalOrderId),
                order.id.eq(orderId)
            )
            .fetch();
        if (results.isEmpty()) {
            throw new OrderNotFoundTotalOrderException();
        }
        return results.get(0);
    }

    @Override
    public TotalOrder findTotalOrderForCancelUpdate(Long totalOrderId) {
        List<TotalOrder> results = queryFactory
            .select(totalOrder)
            .from(totalOrder)
            .join(totalOrder.orders, order).fetchJoin()
            .join(order.orderProducts, orderProduct)
            .where(totalOrder.id.eq(totalOrderId))
            .fetch();

        if (results.isEmpty()) {
            throw new OrderNotFoundTotalOrderException();
        }

        return results.get(0);
    }

    public TotalOrder getTotalOrderDetailInfo(BooleanBuilder totalOrderDetailFilter) {
        List<TotalOrder> results = queryFactory
            .selectDistinct(totalOrder)
            .from(totalOrder)
            .join(totalOrder.totalOrderPayments, totalOrderPayment)
            .join(totalOrder.orders, order).fetchJoin()
            .join(totalOrder.orderer, orderer).fetchJoin()
            .join(order.orderProducts, orderProduct)
            .where(totalOrderDetailFilter)
            .fetch();
        if (results.isEmpty()) {
            throw new OrderNotFoundTotalOrderException();
        }
        return results.get(0);
    }

    private List<TotalOrder> getTotalOrder(GetTotalOrdersUserInfoDto getTotalOrdersUserInfoDto,
        Pageable pageable, BooleanBuilder totalOrderFilter) {
        OrderSpecifier<?>[] orderSpecifiers = getOrderSpecifiers(pageable);
        List<Long> totalOrderIds = queryFactory
            .select(totalOrder.id)
            .from(totalOrder)
            .where(totalOrderFilter)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return queryFactory
            .selectDistinct(totalOrder)
            .from(totalOrder)
            .join(totalOrder.orders, order).fetchJoin()
            .join(order.orderProducts, orderProduct)
            .join(totalOrder.orderer, orderer).fetchJoin()
            .orderBy(orderSpecifiers)
            .where(totalOrder.id.in(totalOrderIds))
            .fetch();
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        Sort sort = pageable.getSort();

        sort.forEach(order -> {
            String sortBy = order.getProperty();
            Order direction = order.getDirection() == Sort.Direction.ASC ? Order.ASC : Order.DESC;

            switch (PageSortBy.valueOf(sortBy.toUpperCase())) {
                case CREATED_AT ->
                    orderSpecifiers.add(new OrderSpecifier<>(direction, totalOrder.createdAt));
                case UPDATED_AT ->
                    orderSpecifiers.add(new OrderSpecifier<>(direction, totalOrder.updatedAt));
                case ID -> orderSpecifiers.add(new OrderSpecifier<>(direction, totalOrder.id));
            }
        });

        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }

    private BooleanBuilder getTotalOrderFilter(GetTotalOrdersUserInfoDto dto) {
        BooleanBuilder builder = new BooleanBuilder();
        List<TotalOrderStatus> targetStatuses = getTotalOrderStatuses();

        builder.and(totalOrder.createdBy.eq(dto.userId()));
        builder.and(totalOrder.status.in(targetStatuses));
        return builder;
    }

    private BooleanBuilder getTotalOrderDetailFilter(GetTotalOrderDetailDto dto) {
        BooleanBuilder builder = new BooleanBuilder();
        List<TotalOrderStatus> targetStatuses = getTotalOrderStatuses();

        builder.and(totalOrder.createdBy.eq(dto.userId()));
        builder.and(totalOrder.id.eq(dto.totalOrderId()));
        builder.and(totalOrder.status.in(targetStatuses));
        return builder;
    }

    private Long totalCount(GetTotalOrdersUserInfoDto getTotalOrdersUserInfoDto,
        BooleanBuilder totalOrderFilter) {
        return queryFactory.select(totalOrder.count())
            .from(totalOrder)
            .where(totalOrderFilter)
            .fetchOne();
    }
}
