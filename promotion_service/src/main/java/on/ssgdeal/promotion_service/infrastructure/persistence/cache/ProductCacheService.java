package on.ssgdeal.promotion_service.infrastructure.persistence.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.application.dto.SliceDto;
import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.ProductOption;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.repository.ProductRepository;
import on.ssgdeal.promotion_service.domain.repository.PromotionRepository;
import on.ssgdeal.promotion_service.exception.ProductException;
import on.ssgdeal.promotion_service.exception.ProductException.ProductCacheSerializeFailedException;
import on.ssgdeal.promotion_service.exception.ProductException.ProductNotFoundException;
import on.ssgdeal.promotion_service.infrastructure.persistence.cache.dto.CachingProductDto;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "ProductCacheService")
@RequiredArgsConstructor
public class ProductCacheService {

    public static final String FIND_PRODUCTS_BY_PROMOTION_PATTERN = "promotion:%d:all_products";
    public static final String PRODUCT_STOCK_KEY_PATTERN = "promotion:product:%d:option:%d";
    public static final String PRODUCT_KEY_PATTERN = "promotion:product:%d";
    private static final Duration CACHE_MARGIN = Duration.ofMinutes(10);


    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    private final Map<Class<?>, ObjectReader> readerCache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getView(Long productId, Class<T> viewType) {
        String key = PRODUCT_KEY_PATTERN.formatted(productId);
        String json = redisTemplate.opsForValue().get(key);
        if (json == null || json.isEmpty()) {
            return Optional.empty();
        }

        ObjectReader reader = readerCache.computeIfAbsent(
            viewType,
            objectMapper::readerFor
        );

        try {
            return Optional.of(reader.readValue(json));
        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.debug("Failed to parse JSON for key {}: {}", key, json);
            }
            return Optional.empty();
        }
    }

    public <T> T getOrLoadView(Long productId, Class<T> viewType) {
        return getView(productId, viewType).orElseGet(() -> {
            Product product = productRepository
                .findById(productId)
                .orElseThrow(ProductNotFoundException::new);

            try {
                CachingProductDto dto = CachingProductDto.from(product);
                String key = PRODUCT_KEY_PATTERN.formatted(dto.getProductId());

                String json = objectMapper.writeValueAsString(dto);

                long ttl = getTtl(productId); // 최소 60초

                redisTemplate.opsForValue().set(key, json, Duration.ofSeconds(ttl));

                ObjectReader reader = readerCache.computeIfAbsent(
                    viewType,
                    objectMapper::readerFor
                );

                return reader.readValue(json);
            } catch (JsonProcessingException e) {
                log.error("JSON processing failed for product ID: {}", productId, e);
                throw new RuntimeException("Failed to process product data", e);
            }
        });
    }

    public <T> SliceDto<T> getAllByPromotionOrLoad(
        Long promotionId,
        Pageable pageable,
        Class<T> viewType
    ) {
        String promotionProductsKey = String.format(FIND_PRODUCTS_BY_PROMOTION_PATTERN,
            promotionId);

        Boolean hasKey = redisTemplate.hasKey(promotionProductsKey);
        List<T> dtos;

        if (Boolean.TRUE.equals(hasKey)) {
            Long total = redisTemplate.opsForList().size(promotionProductsKey);
            if (total == null || total == 0) {
                return SliceDto.from(new SliceImpl<>(List.of(), pageable, false));
            }

            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize() - 1, total.intValue() - 1);

            if (start > end) {
                return SliceDto.from(new SliceImpl<>(List.of(), pageable, false));
            }

            List<String> jsons = redisTemplate.opsForList().range(promotionProductsKey, start, end);
            if (jsons == null || jsons.isEmpty()) {
                return SliceDto.from(new SliceImpl<>(List.of(), pageable, false));
            }

            ObjectReader reader = readerCache.computeIfAbsent(viewType, objectMapper::readerFor);
            dtos = jsons.stream()
                .map(js -> {
                    try {
                        return (T) reader.readValue(js);
                    } catch (IOException e) {
                        log.warn("Failed to parse json: {}", js, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

            boolean hasNext = end < total - 1;
            return SliceDto.from(new SliceImpl<>(dtos, pageable, hasNext));
        } else {
            Promotion promotion = promotionRepository.findById(promotionId).orElseThrow(
                ProductException.ProductPromotionIsNotInProgressException::new
            );
            Long companyId = promotion.getCompany().getId();

            Slice<Product> productSlice = productRepository.findByCompanyId(companyId, pageable);
            List<Product> products = productSlice.getContent();

            ObjectReader reader = readerCache.computeIfAbsent(viewType, objectMapper::readerFor);
            List<T> loaded = new ArrayList<>(products.size());

            redisTemplate.execute(new SessionCallback<Void>() {
                @Override
                public Void execute(RedisOperations operations) throws DataAccessException {
                    operations.multi();

                    for (Product p : products) {
                        try {
                            CachingProductDto cacheDto = CachingProductDto.from(p);
                            String json = objectMapper.writeValueAsString(cacheDto);

                            String cacheKey = PRODUCT_KEY_PATTERN.formatted(p.getId());

                            operations.opsForValue()
                                .set(cacheKey, json, Duration.ofSeconds(getTtl(p.getId())));

                            operations.opsForList().rightPush(promotionProductsKey, json);

                            loaded.add(reader.readValue(json));
                        } catch (IOException e) {
                            log.warn("Failed serialize or parse for product {}", p.getId(), e);
                        }
                    }

                    operations.expire(promotionProductsKey, Duration.ofHours(1));

                    operations.exec();
                    return null;
                }
            });

            return SliceDto.from(new SliceImpl<>(loaded, pageable, productSlice.hasNext()));
        }
    }

    public void saveProductStockListCache(List<Product> products) {
        for (Product product : products) {
            Long ttl = getTtl(product.getId());
            for (ProductOption option : product.getOptions()) {
                String key = String.format(PRODUCT_STOCK_KEY_PATTERN, product.getId(),
                    option.getId());
                String value = String.valueOf(option.getProductStock().getValue());
                redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttl));
            }
        }
    }

    public void saveProductListCache(List<CachingProductDto> dtos) {
        for (CachingProductDto dto : dtos) {
            saveProductCache(dto);
        }
    }

    public void saveProductCache(CachingProductDto dto) {
        try {
            Long ttl = getTtl(dto.getProductId());
            if (ttl == 0L) {
                return;
            }
            String json = objectMapper.writeValueAsString(dto);
            String key = PRODUCT_KEY_PATTERN.formatted(dto.getProductId());
            redisTemplate.opsForValue().set(key, json, Duration.ofSeconds(ttl));
        } catch (JsonProcessingException e) {
            throw new ProductCacheSerializeFailedException();
        }
    }

    public void evictProductCache(Long productId) {
        String key = PRODUCT_KEY_PATTERN.formatted(productId);
        redisTemplate.delete(key);
    }

    public Long getTtl(Long productId) {
        Product product = productRepository.findWithPromotionById(productId)
            .orElseThrow(ProductNotFoundException::new);
        LocalDate today = LocalDate.now();
        LocalDate endDate = product
            .getCompany()
            .getPromotion()
            .getEndPromotionDate();

        long days = ChronoUnit.DAYS.between(today, endDate);
        if (days <= 0) {
            return 3600L;
        }
        return Duration.ofDays(days).plus(CACHE_MARGIN).getSeconds();
    }


}
