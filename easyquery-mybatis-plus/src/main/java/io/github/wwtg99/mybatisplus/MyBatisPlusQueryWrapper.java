package io.github.wwtg99.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.wwtg99.core.IQueryBuilder;
import io.github.wwtg99.core.QueryFilterAction;
import io.github.wwtg99.core.SearchStrategy;
import io.github.wwtg99.core.entry.*;
import io.github.wwtg99.core.extractor.ExtractorHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyBatisPlusQueryWrapper<E> implements IQueryBuilder<QueryWrapper<E>> {

    private final ExtractorHolder extractorHolder;

    @Override
    public QueryWrapper<E> build(QueryWrapper<E> wrapper, Object query) {
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
    public QueryWrapper<E> newQueryWrapper() {
        return new QueryWrapper<>();
    }

    private void buildFilter(QueryWrapper<E> wrapper, FilterEntry entry) {
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
                if (val instanceof Iterable<?> && ((Iterable<?>) val).iterator().hasNext()) {
                    wrapper.in(field, val);
                }
            }
            case NOT_IN -> {
                // val must be Iterable and not empty
                if (val instanceof Iterable<?> && ((Iterable<?>) val).iterator().hasNext()) {
                    wrapper.notIn(field, val);
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
            case STARTS_WITH -> wrapper.likeRight(field, val);
            case ENDS_WITH -> wrapper.likeLeft(field, val);
        }
    }

    private void buildSorter(QueryWrapper<E> wrapper, SortEntry entry) {
        String field = entry.getField();
        boolean asc = entry.isAsc();
        if (asc) {
            wrapper.orderByAsc(field);
        } else {
            wrapper.orderByDesc(field);
        }
    }

    private void buildSearcher(QueryWrapper<E> wrapper, SearchEntry entry) {
        String[] fields = entry.getFields();
        SearchStrategy strategy = entry.getSearchStrategy();
        Object val = entry.getValue();
        switch (strategy) {
            case STARTS_WITH -> wrapper.and(
                    wrapper1 -> {
                        for (String field : fields) {
                            wrapper1.or(wrapper2 -> wrapper2.likeRight(field, val));
                        }
                    });
            case ENDS_WITH -> wrapper.and(
                    wrapper1 -> {
                        for (String field : fields) {
                            wrapper1.or(wrapper2 -> wrapper2.likeLeft(field, val));
                        }
                    });
            case BOTH -> wrapper.and(
                    wrapper1 -> {
                        for (String field : fields) {
                            wrapper1.or(wrapper2 -> wrapper2.like(field, val));
                        }
                    });
        }
    }
}
