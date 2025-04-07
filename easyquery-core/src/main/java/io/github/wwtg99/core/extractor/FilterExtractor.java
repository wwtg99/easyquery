package io.github.wwtg99.core.extractor;

import io.github.wwtg99.core.QueryFilterAction;
import io.github.wwtg99.core.WrapperUtils;
import io.github.wwtg99.core.annotation.QueryFilter;
import io.github.wwtg99.core.entry.FilterEntry;
import io.github.wwtg99.core.entry.IQueryEntry;
import io.github.wwtg99.core.processor.IValueProcessor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * Extractor for filter.
 *
 * @author wwtg99
 */
public class FilterExtractor implements IFieldExtractor {
    @Override
    public IQueryEntry extractField(Field field, String fieldName, Object fieldValue) {
        // skip if field value is null
        if (Objects.isNull(fieldValue)) {
            return null;
        }
        // get filter annotation
        QueryFilter anno = field.getAnnotation(QueryFilter.class);
        if (Objects.isNull(anno)) {
            return null;
        }
        // get field type
        Class<?> valType = field.getType();
        // get filter action
        QueryFilterAction action = anno.action();
        // parse field name
        String parsedName = WrapperUtils.isStrNotEmpty(anno.value()) ? anno.value() : fieldName;
        // preprocess field value
        try {
            IValueProcessor valueProcessor =
                    anno.processorFactory()
                            .getDeclaredConstructor()
                            .newInstance()
                            .createValueProcessor();
            fieldValue = valueProcessor.processValue(fieldValue);
        } catch (InvocationTargetException
                | InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | NoSuchMethodException
                | SecurityException ignored) {
            // skip for exception
            return null;
        }
        return new FilterEntry(field.getName(), parsedName, fieldValue, valType, action);
    }
}
