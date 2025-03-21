package io.github.wwtg99.core.entry;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SortEntry implements IQueryEntry {
    private String entryName;
    private String field;
    private boolean asc;
}
