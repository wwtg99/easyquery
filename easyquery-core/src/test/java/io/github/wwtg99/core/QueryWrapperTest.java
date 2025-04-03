package io.github.wwtg99.core;

import io.github.wwtg99.core.annotation.QueryFilter;
import io.github.wwtg99.core.annotation.QuerySearcher;
import io.github.wwtg99.core.annotation.QuerySorter;
import io.github.wwtg99.core.entry.FilterEntry;
import io.github.wwtg99.core.entry.IQueryEntry;
import io.github.wwtg99.core.entry.SearchEntry;
import io.github.wwtg99.core.entry.SortEntry;
import io.github.wwtg99.core.extractor.ExtractorHolder;
import io.github.wwtg99.core.extractor.FilterExtractor;
import io.github.wwtg99.core.extractor.SearcherExtractor;
import io.github.wwtg99.core.extractor.SorterExtractor;
import io.github.wwtg99.core.processor.DefaultValueProcessor;
import io.github.wwtg99.core.processor.DefaultValueProcessorFactory;
import io.github.wwtg99.core.processor.IValueProcessor;
import java.lang.reflect.Field;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("QueryWrapper Test")
public class QueryWrapperTest {

    @Data
    @AllArgsConstructor
    static class Obj1 {
        @QueryFilter private String name;
        @QueryFilter private Integer age;

        @QueryFilter(value = "time", action = QueryFilterAction.GE)
        private String startTime;

        private String noFilter;
    }

    @Data
    @AllArgsConstructor
    static class Obj2 {
        @QuerySearcher private String item;

        @QuerySearcher(
                value = {"name", "label"},
                searchStrategy = SearchStrategy.BOTH)
        private String query;

        private String noAnno;
    }

    @Data
    @AllArgsConstructor
    static class Obj3 {
        @QuerySorter private Boolean order;

        @QuerySorter(
                value = "order",
                trueForAsc = false,
                enableForNull = true,
                defaultDirection = SorterDirection.DESC)
        private Boolean order2;

        @QuerySorter private Integer order3;

        private Boolean noAnno;
    }

    @Data
    @AllArgsConstructor
    static class Obj4 {
        @QueryFilter private String name;
        @QuerySearcher private String query;

        @QueryFilter(action = QueryFilterAction.GE)
        private Integer age;

        @QuerySorter private Boolean date;

        private String noAnno;
    }

    @Test
    public void testDefaultValueProcessor() {
        DefaultValueProcessorFactory factory = new DefaultValueProcessorFactory();
        IValueProcessor processor = factory.createValueProcessor();
        Assertions.assertInstanceOf(DefaultValueProcessor.class, processor);
        Object ret = processor.processValue(null);
        Assertions.assertNull(ret);
        ret = processor.processValue("");
        Assertions.assertEquals("", ret);
        ret = processor.processValue("wwtg99");
        Assertions.assertEquals("wwtg99", ret);
        ret = processor.processValue(100);
        Assertions.assertEquals(100, ret);
    }

    @Test
    @SneakyThrows
    public void testFilterExtractor() {
        // field name
        String name = "name";
        String fieldValue1 = "wwtg99";
        Field field = Obj1.class.getDeclaredField(name);
        FilterExtractor extractor = new FilterExtractor();
        IQueryEntry entry = extractor.extractField(field, name, fieldValue1);
        Assertions.assertNotNull(entry);
        Assertions.assertEquals(name, entry.getEntryName());
        Assertions.assertInstanceOf(FilterEntry.class, entry);
        FilterEntry entry1 = (FilterEntry) entry;
        Assertions.assertEquals(name, entry1.getField());
        Assertions.assertEquals(QueryFilterAction.EQ, entry1.getAction());
        Assertions.assertEquals(fieldValue1, entry1.getValue());
        Assertions.assertEquals(String.class, entry1.getValType());
        Assertions.assertNull(extractor.extractField(field, name, null));
        // field age
        name = "age";
        Integer fieldValue2 = 1;
        field = Obj1.class.getDeclaredField(name);
        entry = extractor.extractField(field, name, fieldValue2);
        Assertions.assertNotNull(entry);
        Assertions.assertEquals(name, entry.getEntryName());
        Assertions.assertInstanceOf(FilterEntry.class, entry);
        entry1 = (FilterEntry) entry;
        Assertions.assertEquals(name, entry1.getField());
        Assertions.assertEquals(QueryFilterAction.EQ, entry1.getAction());
        Assertions.assertEquals(fieldValue2, entry1.getValue());
        Assertions.assertEquals(Integer.class, entry1.getValType());
        // field startTime
        name = "startTime";
        String fieldValue3 = "2025-01-01";
        field = Obj1.class.getDeclaredField(name);
        entry = extractor.extractField(field, name, fieldValue3);
        Assertions.assertNotNull(entry);
        Assertions.assertEquals(name, entry.getEntryName());
        Assertions.assertInstanceOf(FilterEntry.class, entry);
        entry1 = (FilterEntry) entry;
        Assertions.assertEquals("time", entry1.getField());
        Assertions.assertEquals(QueryFilterAction.GE, entry1.getAction());
        Assertions.assertEquals(fieldValue3, entry1.getValue());
        Assertions.assertEquals(String.class, entry1.getValType());
        // field noFilter
        name = "noFilter";
        String fieldValue4 = "no filter";
        field = Obj1.class.getDeclaredField(name);
        entry = extractor.extractField(field, name, fieldValue4);
        Assertions.assertNull(entry);
    }

    @Test
    @SneakyThrows
    public void testSearcherExtractor() {
        // field item
        String name = "item";
        String fieldValue1 = "query";
        Field field = Obj2.class.getDeclaredField(name);
        SearcherExtractor extractor = new SearcherExtractor();
        IQueryEntry entry = extractor.extractField(field, name, fieldValue1);
        Assertions.assertNotNull(entry);
        Assertions.assertEquals(name, entry.getEntryName());
        Assertions.assertInstanceOf(SearchEntry.class, entry);
        SearchEntry entry1 = (SearchEntry) entry;
        Assertions.assertArrayEquals(new String[] {name}, entry1.getFields());
        Assertions.assertEquals(SearchStrategy.STARTS_WITH, entry1.getSearchStrategy());
        Assertions.assertEquals(fieldValue1, entry1.getValue());
        Assertions.assertNull(extractor.extractField(field, name, null));
        // field query
        name = "query";
        String fieldValue2 = "query";
        field = Obj2.class.getDeclaredField(name);
        entry = extractor.extractField(field, name, fieldValue2);
        Assertions.assertNotNull(entry);
        Assertions.assertEquals(name, entry.getEntryName());
        Assertions.assertInstanceOf(SearchEntry.class, entry);
        entry1 = (SearchEntry) entry;
        Assertions.assertArrayEquals(new String[] {"name", "label"}, entry1.getFields());
        Assertions.assertEquals(SearchStrategy.BOTH, entry1.getSearchStrategy());
        Assertions.assertEquals(fieldValue2, entry1.getValue());
        // field query
        name = "noAnno";
        String fieldValue3 = "no anno";
        field = Obj2.class.getDeclaredField(name);
        entry = extractor.extractField(field, name, fieldValue3);
        Assertions.assertNull(entry);
    }

    @Test
    @SneakyThrows
    public void testSorterExtractor() {
        // field order
        String name = "order";
        Boolean fieldValue1 = true;
        Field field = Obj3.class.getDeclaredField(name);
        SorterExtractor extractor = new SorterExtractor();
        IQueryEntry entry = extractor.extractField(field, name, fieldValue1);
        Assertions.assertNotNull(entry);
        Assertions.assertEquals(name, entry.getEntryName());
        Assertions.assertInstanceOf(SortEntry.class, entry);
        SortEntry entry1 = (SortEntry) entry;
        Assertions.assertEquals(name, entry1.getField());
        Assertions.assertTrue(entry1.isAsc());
        fieldValue1 = false;
        entry = extractor.extractField(field, name, fieldValue1);
        Assertions.assertNotNull(entry);
        Assertions.assertEquals(name, entry.getEntryName());
        Assertions.assertInstanceOf(SortEntry.class, entry);
        entry1 = (SortEntry) entry;
        Assertions.assertEquals(name, entry1.getField());
        Assertions.assertFalse(entry1.isAsc());
        fieldValue1 = null;
        entry = extractor.extractField(field, name, fieldValue1);
        Assertions.assertNull(entry);
        Assertions.assertNull(extractor.extractField(field, name, null));
        // field order2
        name = "order2";
        Boolean fieldValue2 = true;
        field = Obj3.class.getDeclaredField(name);
        entry = extractor.extractField(field, name, fieldValue2);
        Assertions.assertNotNull(entry);
        Assertions.assertEquals(name, entry.getEntryName());
        Assertions.assertInstanceOf(SortEntry.class, entry);
        entry1 = (SortEntry) entry;
        Assertions.assertEquals("order", entry1.getField());
        Assertions.assertFalse(entry1.isAsc());
        fieldValue2 = false;
        entry = extractor.extractField(field, name, fieldValue2);
        Assertions.assertNotNull(entry);
        Assertions.assertEquals(name, entry.getEntryName());
        Assertions.assertInstanceOf(SortEntry.class, entry);
        entry1 = (SortEntry) entry;
        Assertions.assertEquals("order", entry1.getField());
        Assertions.assertTrue(entry1.isAsc());
        fieldValue2 = null;
        entry = extractor.extractField(field, name, fieldValue2);
        Assertions.assertNotNull(entry);
        Assertions.assertEquals(name, entry.getEntryName());
        Assertions.assertInstanceOf(SortEntry.class, entry);
        entry1 = (SortEntry) entry;
        Assertions.assertEquals("order", entry1.getField());
        Assertions.assertFalse(entry1.isAsc());
        // field order3
        name = "order3";
        Integer fieldValue3 = 1;
        field = Obj3.class.getDeclaredField(name);
        entry = extractor.extractField(field, name, fieldValue3);
        Assertions.assertNull(entry);
        // field no anno
        name = "noAnno";
        Boolean fieldValue4 = true;
        field = Obj3.class.getDeclaredField(name);
        entry = extractor.extractField(field, name, fieldValue4);
        Assertions.assertNull(entry);
    }

    @Test
    public void testExtractHolder() {
        ExtractorHolder holder = new ExtractorHolder();
        holder.addExtractor(new FilterExtractor())
                .addExtractor(new SearcherExtractor())
                .addExtractor(new SorterExtractor());
        List<IQueryEntry> entries = holder.extract(null);
        Assertions.assertEquals(0, entries.size());
        Obj4 obj4 = new Obj4("name", "query", 30, true, "no");
        entries = holder.extract(obj4);
        Assertions.assertEquals(4, entries.size());
        Assertions.assertInstanceOf(FilterEntry.class, entries.get(0));
        Assertions.assertEquals("name", entries.get(0).getEntryName());
        Assertions.assertInstanceOf(SearchEntry.class, entries.get(1));
        Assertions.assertEquals("query", entries.get(1).getEntryName());
        Assertions.assertInstanceOf(FilterEntry.class, entries.get(2));
        Assertions.assertEquals("age", entries.get(2).getEntryName());
        Assertions.assertInstanceOf(SortEntry.class, entries.get(3));
        Assertions.assertEquals("date", entries.get(3).getEntryName());
    }
}
