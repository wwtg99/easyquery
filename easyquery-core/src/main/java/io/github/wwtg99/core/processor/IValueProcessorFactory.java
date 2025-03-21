package io.github.wwtg99.core.processor;

/**
 * Interface for value processor factory.
 * @author wwtg99
 */
public interface IValueProcessorFactory {

    /**
     * Create value processor.
     * @return IValueProcessor
     */
    IValueProcessor createValueProcessor();
}
