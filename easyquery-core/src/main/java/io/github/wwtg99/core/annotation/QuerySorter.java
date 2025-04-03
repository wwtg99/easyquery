package io.github.wwtg99.core.annotation;

import io.github.wwtg99.core.SorterDirection;
import java.lang.annotation.*;

/**
 * Annotation for field sorter. Annotated on class field to indicate sorter.
 *
 * @author wwtg99
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface QuerySorter {
    String value() default "";

    boolean trueForAsc() default true;

    boolean enableForNull() default false;

    SorterDirection defaultDirection() default SorterDirection.ASC;
}
