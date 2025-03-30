package io.github.wwtg99.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.wwtg99.core.QueryFilterAction;
import io.github.wwtg99.core.SearchStrategy;
import io.github.wwtg99.core.SorterDirection;
import io.github.wwtg99.core.annotation.QueryFilter;
import io.github.wwtg99.core.annotation.QuerySearcher;
import io.github.wwtg99.core.annotation.QuerySorter;
import io.github.wwtg99.core.extractor.ExtractorHolder;
import io.github.wwtg99.core.extractor.FilterExtractor;
import io.github.wwtg99.core.extractor.SearcherExtractor;
import io.github.wwtg99.core.extractor.SorterExtractor;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

@DisplayName("MyBatisPlus Test")
public class MyBatisPlusTest {

    @Data
    @Builder
    static class Obj1 {
        @QueryFilter
        private String name;
        @QueryFilter(value = "age", action = QueryFilterAction.GT)
        private Integer ageGt;
        @QueryFilter(value = "age", action = QueryFilterAction.LT)
        private Integer ageLt;
        @QueryFilter(value = "name", action = QueryFilterAction.NE)
        private String nameNe;
        @QueryFilter(action = QueryFilterAction.CONTAINS)
        private String address;
        @QueryFilter(value = "size", action = QueryFilterAction.GE)
        private Integer sizeGe;
        @QueryFilter(value = "size", action = QueryFilterAction.LE)
        private Integer sizeLe;
        @QueryFilter(value = "type", action = QueryFilterAction.IN)
        private List<String> types;
        @QueryFilter(value = "type", action = QueryFilterAction.NOT_IN)
        private List<String> noTypes;
        @QueryFilter(value = "flag", action = QueryFilterAction.IS_NULL)
        private Boolean isNull;
        @QueryFilter(value = "flag", action = QueryFilterAction.IS_NOT_NULL)
        private Boolean isNotNull;
        @QuerySearcher({"name", "address"})
        private String query;
    }

    @Data
    @Builder
    static class Obj2 {
        @QuerySearcher(value = {"name"}, searchStrategy = SearchStrategy.STARTS_WITH)
        private String leftLike;
        @QuerySearcher(value = {"name"}, searchStrategy = SearchStrategy.ENDS_WITH)
        private String rightLike;
        @QuerySearcher(value = {"name"}, searchStrategy = SearchStrategy.BOTH)
        private String bothLike;
        @QuerySorter
        private Boolean sort;
        @QuerySorter(value = "date", trueForAsc = false, enableForNull = true, defaultDirection = SorterDirection.DESC)
        private Boolean dateDesc;
    }

    @Test
    public void testQueryWrapper() {
        ExtractorHolder extractorHolder = new ExtractorHolder();
        extractorHolder.addExtractor(new FilterExtractor()).addExtractor(new SorterExtractor()).addExtractor(new SearcherExtractor());
        MyBatisPlusQueryWrapper<?> wrapper = new MyBatisPlusQueryWrapper<>(extractorHolder);
        Obj1 obj1 = Obj1.builder().name("name").ageGt(30).build();
        QueryWrapper<?> queryWrapper = wrapper.build(obj1);
        Assertions.assertEquals("(name = ? AND age > ?)", queryWrapper.getTargetSql());
        obj1 = Obj1.builder().name("name").ageGt(35).ageLt(40).nameNe("no").address("address").query("query").build();
        queryWrapper = wrapper.build(obj1);
        Assertions.assertEquals("(name = ? AND age > ? AND age < ? AND name <> ? AND ((address LIKE ?) OR (name LIKE ?)))", queryWrapper.getTargetSql());
        obj1 = Obj1.builder().sizeGe(100).sizeLe(200).types(List.of("t1", "t2")).noTypes(List.of("t3")).isNull(true).build();
        queryWrapper = wrapper.build(obj1);
        Assertions.assertEquals("(size >= ? AND size <= ? AND type IN (?) AND type NOT IN (?) AND flag IS NULL)", queryWrapper.getTargetSql().trim());
        obj1 = Obj1.builder().sizeGe(100).types(Collections.emptyList()).isNotNull(true).build();
        queryWrapper = wrapper.build(obj1);
        Assertions.assertEquals("(size >= ? AND flag IS NOT NULL)", queryWrapper.getTargetSql().trim());
        obj1 = Obj1.builder().types(List.of("t1")).name("name").isNull(false).build();
        queryWrapper = wrapper.build(obj1);
        Assertions.assertEquals("(name = ? AND type IN (?))", queryWrapper.getTargetSql().trim());
        Obj2 obj2 = Obj2.builder().leftLike("left").rightLike("right").bothLike("both").sort(false).dateDesc(null).build();
        queryWrapper = wrapper.build(obj2);
        Assertions.assertEquals("(((name LIKE ?)) AND ((name LIKE ?)) AND ((name LIKE ?))) ORDER BY sort DESC,date DESC", queryWrapper.getTargetSql());
        obj2 = Obj2.builder().bothLike("both").sort(true).dateDesc(true).build();
        queryWrapper = wrapper.build(obj2);
        Assertions.assertEquals("(((name LIKE ?))) ORDER BY sort ASC,date DESC", queryWrapper.getTargetSql());
        obj2 = Obj2.builder().bothLike("both").sort(null).dateDesc(false).build();
        queryWrapper = wrapper.build(obj2);
        Assertions.assertEquals("(((name LIKE ?))) ORDER BY date ASC", queryWrapper.getTargetSql());
    }
}
