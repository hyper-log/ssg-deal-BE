package on.ssgdeal.promotion_service.infrastructure.persistence.jpa.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.pageable.enums.PageSortBy;
import on.ssgdeal.promotion_service.domain.entity.Company;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.dto.GetCompaniesConditionDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetInProgressPromotionDetailDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetPromotionsConditionDto;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static on.ssgdeal.promotion_service.domain.entity.QCompany.company;
import static on.ssgdeal.promotion_service.domain.entity.QProduct.product;
import static on.ssgdeal.promotion_service.domain.entity.QPromotion.promotion;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PromotionQueryDslRepositoryImpl implements PromotionQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<GetInProgressPromotionDetailDto> findPromotionWithProductsById(
            Long promotionId,
            Pageable pageable
    ) {
        Tuple promotionInfo = queryFactory
                .select(
                        promotion.id,
                        promotion.title,
                        promotion.contentImageUrl.value,
                        promotion.content,
                        company.id,
                        company.name.value,
                        company.logoUrl.value,
                        promotion.startPromotionDate,
                        promotion.endPromotionDate
                )
                .from(promotion)
                .join(promotion.company, company)
                .where(promotion.id.eq(promotionId))
                .fetchOne();

        if (promotionInfo == null) {
            return Optional.empty();
        }

        List<GetInProgressPromotionDetailDto.ProductDetailDto> products = queryFactory
                .select(Projections.constructor(GetInProgressPromotionDetailDto.ProductDetailDto.class,
                        product.id.as("productId"),
                        product.name.value.as("productName"),
                        product.previewUrl.value.as("previewUrl"),
                        product.originalPrice.value.as("originalPrice"),
                        product.promotionPrice.value.as("promotionPrice")
                ))
                .from(product)
                .where(product.company.id.eq(promotionInfo.get(company.id)))
                .orderBy(product.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = products.size() > pageable.getPageSize();

        //TODO : Slice에 경우 다음 값에 대한 정보 추가
        Slice<GetInProgressPromotionDetailDto.ProductDetailDto> productSlice =
                new SliceImpl<>(products, pageable, hasNext);

        return Optional.of(new GetInProgressPromotionDetailDto(
                promotionInfo.get(promotion.id),
                promotionInfo.get(promotion.title),
                promotionInfo.get(promotion.contentImageUrl.value),
                promotionInfo.get(promotion.content),
                promotionInfo.get(company.name.value),
                promotionInfo.get(company.logoUrl.value),
                promotionInfo.get(promotion.startPromotionDate),
                promotionInfo.get(promotion.endPromotionDate),
                productSlice
        ));
    }

    @Override
    public Page<Promotion> findPromotions(GetPromotionsConditionDto conditionDto) {
        OrderSpecifier<?>[] orderSpecifiers = getOrderPromotionSpecifiers(conditionDto.pageable());
        List<Promotion> promotions = queryFactory
                .select(promotion)
                .from(promotion)
                .where(
                        containsKeyword(conditionDto.keyword(), promotion.title),
                        filterStatus(conditionDto.filter())
                )
                .orderBy(orderSpecifiers)
                .offset(conditionDto.pageable().getOffset())
                .limit(conditionDto.pageable().getPageSize())
                .fetch();

        Long totalCountResult = queryFactory
                .select(promotion.count())
                .from(promotion)
                .where(
                        containsKeyword(conditionDto.keyword(), promotion.title),
                        filterStatus(conditionDto.filter())
                )
                .fetchOne();

        long totalCount = (totalCountResult != null) ? totalCountResult : 0L;

        return new PageImpl<>(promotions, conditionDto.pageable(), totalCount);
    }

    @Override
    public Page<Company> findCompanies(GetCompaniesConditionDto conditionDto) {
        OrderSpecifier<?>[] orderSpecifiers = getOrderCompanySpecifiers(conditionDto.pageable());

        List<Company> companies = queryFactory
                .select(company)
                .from(company)
                .where(
                        containsKeyword(conditionDto.keyword(), company.name.value)
                )
                .orderBy(orderSpecifiers)
                .offset(conditionDto.pageable().getOffset())
                .limit(conditionDto.pageable().getPageSize())
                .fetch();

        Long totalCountResult = queryFactory
                .select(company.count())
                .from(company)
                .where(
                        containsKeyword(conditionDto.keyword(), company.name.value)
                )
                .fetchOne();

        long totalCount = (totalCountResult != null) ? totalCountResult : 0L;

        return new PageImpl<>(companies, conditionDto.pageable(), totalCount);
    }

    private BooleanExpression containsKeyword(String keyword, StringPath target) {
        return keyword != null ? target.containsIgnoreCase(keyword) : null;
    }

    private BooleanExpression filterStatus(PromotionStatus filter) {
        return filter != null ? promotion.status.eq(filter) : null;
    }

    private OrderSpecifier<?>[] getOrderCompanySpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        Sort sort = pageable.getSort();
        sort.forEach(order -> {
            String sortBy = order.getProperty();
            Order direction = order.getDirection() == Sort.Direction.ASC ? Order.ASC : Order.DESC;
            switch (PageSortBy.from(sortBy)) {
                case CREATED_AT -> orderSpecifiers.add(new OrderSpecifier<>(direction, company.createdAt));
                case UPDATED_AT -> orderSpecifiers.add(new OrderSpecifier<>(direction, company.updatedAt));
                case ID -> orderSpecifiers.add(new OrderSpecifier<>(direction, company.id));
            }
        });
        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }

    private OrderSpecifier<?>[] getOrderPromotionSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        Sort sort = pageable.getSort();
        sort.forEach(order -> {
            String sortBy = order.getProperty();
            Order direction = order.getDirection() == Sort.Direction.ASC ? Order.ASC : Order.DESC;
            System.out.println("sortBy = " + sortBy);
            switch (PageSortBy.from(sortBy)) {
                case CREATED_AT -> orderSpecifiers.add(new OrderSpecifier<>(direction, promotion.createdAt));
                case UPDATED_AT -> orderSpecifiers.add(new OrderSpecifier<>(direction, promotion.updatedAt));
                case ID -> orderSpecifiers.add(new OrderSpecifier<>(direction, promotion.id));
            }
        });
        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }

}