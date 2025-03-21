package io.github.wwtg99.core.extractor;

import io.github.wwtg99.core.SearchStrategy;
import io.github.wwtg99.core.WrapperUtils;
import io.github.wwtg99.core.annotation.QuerySearcher;
import io.github.wwtg99.core.entry.IQueryEntry;
import io.github.wwtg99.core.entry.SearchEntry;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class SearcherExtractor implements IFieldExtractor {
    @Override
    public IQueryEntry extractField(Field field, String fieldName, Object fieldValue) {
        // skip if field value is null
        if (Objects.isNull(fieldValue)) {
            return null;
        }
        // get searcher annotation
        QuerySearcher anno = field.getAnnotation(QuerySearcher.class);
        if (Objects.isNull(anno)) {
            return null;
        }
        // get search strategy
        SearchStrategy strategy = anno.searchStrategy();
        // parse field name
        String[] parsedNames;
        Set<String> annoValues = Optional.ofNullable(anno.value()).map(v -> Arrays.stream(v).filter(WrapperUtils::isStrNotEmpty).collect(Collectors.toSet())).orElse(Collections.emptySet());
        if (!annoValues.isEmpty()) {
            parsedNames = annoValues.toArray(new String[]{});
        } else {
            parsedNames = new String[]{fieldName};
        }
        return new SearchEntry(field.getName(), parsedNames, fieldValue, strategy);
    }
}
