package io.github.wwtg99.core.processor;

/**
 * Default value processor.
 * Do nothing.
 * @author wwtg99
 */
public class DefaultValueProcessor implements IValueProcessor {
    @Override
    public Object processValue(Object value) {
        return value;
    }
}
