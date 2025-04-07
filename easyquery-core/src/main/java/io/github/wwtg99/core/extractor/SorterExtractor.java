package io.github.wwtg99.core.extractor;

import io.github.wwtg99.core.SorterDirection;
import io.github.wwtg99.core.WrapperUtils;
import io.github.wwtg99.core.annotation.QuerySorter;
import io.github.wwtg99.core.entry.IQueryEntry;
import io.github.wwtg99.core.entry.SortEntry;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Extractor for sorter
 *
 * @author wwtg99
 */
public class SorterExtractor implements IFieldExtractor {
    @Override
    public IQueryEntry extractField(Field field, String fieldName, Object fieldValue) {
        // get sorter annotation
        QuerySorter anno = field.getAnnotation(QuerySorter.class);
        if (Objects.isNull(anno)) {
            return null;
        }
        // if enable for null
        boolean enableForNull = anno.enableForNull();
        // get default direction
        SorterDirection direction = anno.defaultDirection();
        // parse field name
        String parsedName = WrapperUtils.isStrNotEmpty(anno.value()) ? anno.value() : fieldName;
        if (enableForNull && Objects.isNull(fieldValue)) {
            boolean asc = SorterDirection.ASC.equals(direction);
            return new SortEntry(field.getName(), parsedName, asc);
        }
        // only support boolean type
        if (fieldValue instanceof Boolean) {
            boolean val = (Boolean) fieldValue;
            boolean asc = anno.trueForAsc() == val;
            return new SortEntry(field.getName(), parsedName, asc);
        }
        return null;
    }
}
