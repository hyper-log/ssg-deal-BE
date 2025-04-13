package on.ssgdeal.promotion_service.infrastructure.persistence.jpa.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.promotion_service.domain.entity.dto.GetInProgressPromotionDetailDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static on.ssgdeal.promotion_service.domain.entity.QCompany.company;
import static on.ssgdeal.promotion_service.domain.entity.QProduct.product;
import static on.ssgdeal.promotion_service.domain.entity.QPromotion.promotion;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QueryDslPromotionRepositoryImpl implements QueryDslPromotionRepository {

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
}