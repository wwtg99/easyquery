package io.github.wwtg99.core.entry;

import io.github.wwtg99.core.QueryFilterAction;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilterEntry implements IQueryEntry {
    private String entryName;
    private String field;
    private Object value;
    private Class<?> valType;
    private QueryFilterAction action;
}
