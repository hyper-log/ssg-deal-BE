package on.ssgdeal.order_service.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderNumberGenerator {

    private final StringRedisTemplate redisTemplate;

    public String generateOrderNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String redisKey = "order_seq:" + datePart;

        Long sequence = redisTemplate.opsForValue().increment(redisKey);

        if (sequence == 1L) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
            Duration duration = Duration.between(now, midnight);
            redisTemplate.expire(redisKey, duration);
        }

        String seqPart = String.format("%04d", sequence);
        return datePart + seqPart;
    }
}