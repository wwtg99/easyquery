package io.github.wwtg99.core.extractor;

import io.github.wwtg99.core.entry.IQueryEntry;
import java.lang.reflect.Field;

/**
 * Field extractor
 *
 * @author wwtg99
 */
public interface IFieldExtractor {

    IQueryEntry extractField(Field field, String fieldName, Object fieldValue);
}
