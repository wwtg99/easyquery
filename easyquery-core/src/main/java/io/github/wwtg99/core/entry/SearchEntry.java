package io.github.wwtg99.core.entry;

import io.github.wwtg99.core.SearchStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchEntry implements IQueryEntry {
    private String entryName;
    private String[] fields;
    private Object value;
    private SearchStrategy searchStrategy;
}
