package io.github.wwtg99.core.annotation;

import io.github.wwtg99.core.SearchStrategy;
import java.lang.annotation.*;

/**
 * Annotation for field searcher. Annotated on class field to indicate search.
 *
 * @author wwtg99
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface QuerySearcher {

    String[] value() default "";

    SearchStrategy searchStrategy() default SearchStrategy.STARTS_WITH;
}
