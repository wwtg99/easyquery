package io.github.wwtg99.core.processor;

/**
 * Default value processor factory.
 * @author wwtg99
 */
public class DefaultValueProcessorFactory implements IValueProcessorFactory {
    @Override
    public IValueProcessor createValueProcessor() {
        return new DefaultValueProcessor();
    }
}
