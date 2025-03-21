package io.github.wwtg99.core.processor;

/**
 * Process value before filter.
 * @author wwtg99
 */
public interface IValueProcessor {

    /**
     * Process value before filter.
     * @param value original value
     * @return processed value
     */
    Object processValue(Object value);
}
