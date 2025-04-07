package io.github.wwtg99.core.extractor;

import io.github.wwtg99.core.WrapperUtils;
import io.github.wwtg99.core.entry.IQueryEntry;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;

/**
 * Extractor holder.
 *
 * @author wwtg99
 */
@Getter
public class ExtractorHolder {

    private final List<IFieldExtractor> extractorList = new ArrayList<>();

    /**
     * Add extractor.
     *
     * @param extractor extractor
     * @return this
     */
    public ExtractorHolder addExtractor(IFieldExtractor extractor) {
        this.extractorList.add(extractor);
        return this;
    }

    /**
     * Extract fields from object.
     *
     * @param query objet
     * @return list of entries
     */
    public List<IQueryEntry> extract(Object query) {
        if (Objects.isNull(query)) {
            return new ArrayList<>();
        }
        List<IQueryEntry> list = new ArrayList<>();
        Class<?> clazz = query.getClass();
        Field[] fields = clazz.getDeclaredFields();
        // iterate fields
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = getFieldValue(field, query);
            list.addAll(this.extractField(field, fieldValue));
        }
        return list;
    }

    /**
     * Get field value from object.
     *
     * @param field field
     * @param obj object
     * @return field value
     */
    private Object getFieldValue(Field field, Object obj) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException | IllegalArgumentException | NullPointerException ignored) {
            return null;
        }
    }

    /**
     * Extract field.
     *
     * @param field field
     * @param fieldValue field value
     * @return QueryEntry
     */
    private List<IQueryEntry> extractField(Field field, Object fieldValue) {
        String fieldName = field.getName();
        // transform camel case to snake case
        if (WrapperUtils.containUpperCase(fieldName)) {
            fieldName = WrapperUtils.toSnakeCase(fieldName);
        }
        List<IQueryEntry> arr = new ArrayList<>();
        // extract
        String finalFieldName = fieldName;
        extractorList.forEach(
                e ->
                        Optional.ofNullable(e.extractField(field, finalFieldName, fieldValue))
                                .ifPresent(arr::add));
        return arr;
    }
}
