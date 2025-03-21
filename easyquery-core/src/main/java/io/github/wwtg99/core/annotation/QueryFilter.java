package io.github.wwtg99.core.annotation;

import io.github.wwtg99.core.QueryFilterAction;
import io.github.wwtg99.core.processor.DefaultValueProcessorFactory;
import io.github.wwtg99.core.processor.IValueProcessorFactory;

import java.lang.annotation.*;

/**
 * Annotation for field filter.
 * Annotated on class field to indicate filter.
 * @author wwtg99
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface QueryFilter {

    /**
     * Override field name, default use object's field name.
     */
    String value() default "";

    /**
     * Query filter action.
     */
    QueryFilterAction action() default QueryFilterAction.EQ;

    /**
     * Factory to create value processor.
     */
    Class<? extends IValueProcessorFactory> processorFactory() default DefaultValueProcessorFactory.class;
}
