package io.github.wwtg99.core.annotation;

import io.github.wwtg99.core.SearchStrategy;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface QuerySearcher {

    String[] value() default "";

    SearchStrategy searchStrategy() default SearchStrategy.STARTS_WITH;
}
