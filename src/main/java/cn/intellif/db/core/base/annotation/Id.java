package cn.intellif.db.core.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
    String columnName() default "id";
    String sqlType() default "";
    Strategy strategy() default Strategy.NORM;
}
