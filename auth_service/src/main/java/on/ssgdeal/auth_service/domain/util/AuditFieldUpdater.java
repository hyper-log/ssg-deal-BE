package on.ssgdeal.auth_service.domain.util;

import java.lang.reflect.Field;
import on.ssgdeal.common.jpa.BaseEntity;
import org.springframework.util.ReflectionUtils;

public class AuditFieldUpdater {

    public static <T extends BaseEntity> void updateAuditFields(T entity, Long newUserId) {
        Class<?> baseEntityClass = BaseEntity.class;

        Field createdByField = ReflectionUtils.findField(baseEntityClass, "createdBy");
        Field updatedByField = ReflectionUtils.findField(baseEntityClass, "updatedBy");

        if (createdByField != null) {
            ReflectionUtils.makeAccessible(createdByField);
            ReflectionUtils.setField(createdByField, entity, newUserId);
        }
        if (updatedByField != null) {
            ReflectionUtils.makeAccessible(updatedByField);
            ReflectionUtils.setField(updatedByField, entity, newUserId);
        }
    }
}