package io.github.wwtg99.core;

import io.github.wwtg99.core.entry.IQueryEntry;
import io.github.wwtg99.core.extractor.ExtractorHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Interface for query wrapper.
 * @author wwtg99
 */
public interface IQueryBuilder<T> {

    /**
     * Build query wrapper.
     * @param query object
     * @return query wrapper
     */
    default T build(Object query) {
        return build(newQueryWrapper(), query);
    }

    /**
     * Build query wrapper.
     * @param wrapper original query wrapper
     * @param query object
     * @return query wrapper
     */
    T build(T wrapper, Object query);

    /**
     * create new query wrapper.
     * @return query wrapper
     */
    T newQueryWrapper();

    /**
     * Get ExtractorHolder.
     * @return ExtractorHolder
     */
    ExtractorHolder getExtractorHolder();

    /**
     * Extract query entry from query object.
     * @param query object
     * @return list of query entry
     */
    default List<IQueryEntry> extract(Object query) {
        return Optional.ofNullable(this.getExtractorHolder()).map(h -> h.extract(query)).orElse(Collections.emptyList());
    }

}
