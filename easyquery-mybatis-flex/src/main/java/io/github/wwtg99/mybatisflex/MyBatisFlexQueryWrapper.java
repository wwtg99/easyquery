package io.github.wwtg99.mybatisflex;

import com.mybatisflex.core.query.QueryWrapper;
import io.github.wwtg99.core.IQueryBuilder;
import io.github.wwtg99.core.QueryFilterAction;
import io.github.wwtg99.core.SearchStrategy;
import io.github.wwtg99.core.entry.FilterEntry;
import io.github.wwtg99.core.entry.SearchEntry;
import io.github.wwtg99.core.entry.SortEntry;
import io.github.wwtg99.core.extractor.ExtractorHolder;
import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyBatisFlexQueryWrapper implements IQueryBuilder<QueryWrapper> {

    private final ExtractorHolder extractorHolder;

    @Override
    public QueryWrapper build(QueryWrapper wrapper, Object query) {
        this.extract(query)
                .forEach(
                        entry -> {
                            if (entry instanceof FilterEntry ent) {
                                this.buildFilter(wrapper, ent);
                            } else if (entry instanceof SortEntry ent) {
                                this.buildSorter(wrapper, ent);
                            } else if (entry instanceof SearchEntry ent) {
                                this.buildSearcher(wrapper, ent);
                            }
                        });
        return wrapper;
    }

    @Override
    public QueryWrapper newQueryWrapper() {
        return new QueryWrapper();
    }

    private void buildFilter(QueryWrapper wrapper, FilterEntry entry) {
        String field = entry.getField();
        Object val = entry.getValue();
        QueryFilterAction action = entry.getAction();
        switch (action) {
            case EQ -> wrapper.eq(field, val);
            case NE -> wrapper.ne(field, val);
            case GE -> wrapper.ge(field, val);
            case GT -> wrapper.gt(field, val);
            case LE -> wrapper.le(field, val);
            case LT -> wrapper.lt(field, val);
            case IN -> {
                // val must be Iterable and not empty
                if (val instanceof Collection<?> && !((Collection<?>) val).isEmpty()) {
                    wrapper.in(field, (Collection<?>) val);
                }
            }
            case NOT_IN -> {
                // val must be Iterable and not empty
                if (val instanceof Collection<?> && !((Collection<?>) val).isEmpty()) {
                    wrapper.notIn(field, (Collection<?>) val);
                }
            }
            case IS_NULL -> {
                // value must be boolean
                if (val instanceof Boolean && (Boolean) val) {
                    wrapper.isNull(field);
                }
            }
            case IS_NOT_NULL -> {
                // value must be boolean
                if (val instanceof Boolean && (Boolean) val) {
                    wrapper.isNotNull(field);
                }
            }
            case CONTAINS -> wrapper.like(field, val);
            case STARTS_WITH -> wrapper.likeLeft(field, val);
            case ENDS_WITH -> wrapper.likeRight(field, val);
        }
    }

    private void buildSorter(QueryWrapper wrapper, SortEntry entry) {
        String field = entry.getField();
        boolean asc = entry.isAsc();
        wrapper.orderBy(field, asc);
    }

    private void buildSearcher(QueryWrapper wrapper, SearchEntry entry) {
        String[] fields = entry.getFields();
        SearchStrategy strategy = entry.getSearchStrategy();
        Object val = entry.getValue();
        switch (strategy) {
            case STARTS_WITH -> wrapper.and(
                    wrapper1 -> {
                        for (String field : fields) {
                            wrapper1.or(wrapper2 -> wrapper2.likeLeft(field, val), true);
                        }
                    });
            case ENDS_WITH -> wrapper.and(
                    wrapper1 -> {
                        for (String field : fields) {
                            wrapper1.or(wrapper2 -> wrapper2.likeRight(field, val), true);
                        }
                    });
            case BOTH -> wrapper.and(
                    wrapper1 -> {
                        for (String field : fields) {
                            wrapper1.or(wrapper2 -> wrapper2.like(field, val), true);
                        }
                    });
        }
    }
}
