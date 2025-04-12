package on.ssgdeal.user_service.infrastructure.persistence.jpa.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.user_service.application.dto.user.SearchUserRequestDto;
import on.ssgdeal.user_service.domain.entity.QUser;
import on.ssgdeal.user_service.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j(topic = "UserQueryRepositoryImpl")
@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository {

    private final JPAQueryFactory queryFactory;
    private QUser user;
    private BooleanBuilder builder;

    @PostConstruct
    public void init() {
        this.user = QUser.user;
        this.builder = new BooleanBuilder();
    }

    @Override
    public Page<User> searchUser(SearchUserRequestDto requestDto) {

        Pageable pageable = requestDto.pageable();

        if (requestDto.nickname() != null && !requestDto.nickname().isEmpty()) {
            builder.and(user.nickname.containsIgnoreCase(requestDto.nickname()));
        }

        if (requestDto.slackEmail() != null && !requestDto.slackEmail().isEmpty()) {
            builder.and(user.slackEmail.email.containsIgnoreCase(requestDto.slackEmail()));
        }

        List<User> content = queryFactory
            .selectFrom(user)
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = queryFactory
            .select(user.id)
            .from(user)
            .where(builder)
            .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }
}
