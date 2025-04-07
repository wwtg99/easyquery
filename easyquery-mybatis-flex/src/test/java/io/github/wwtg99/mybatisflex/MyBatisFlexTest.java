package io.github.wwtg99.mybatisflex;

import com.mybatisflex.core.query.QueryWrapper;
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
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MyBatisFlex Test")
public class MyBatisFlexTest {

    private static final String TABLE_NAME = "table";

    @Data
    @Builder
    static class Obj1 {
        @QueryFilter private String name;

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

        @QueryFilter(value = "name", action = QueryFilterAction.STARTS_WITH)
        private String startsWith;

        @QueryFilter(value = "name", action = QueryFilterAction.ENDS_WITH)
        private String endsWith;

        @QuerySearcher({"name", "address"})
        private String query;
    }

    @Data
    @Builder
    static class Obj2 {
        @QuerySearcher(
                value = {"name"},
                searchStrategy = SearchStrategy.STARTS_WITH)
        private String leftLike;

        @QuerySearcher(
                value = {"name"},
                searchStrategy = SearchStrategy.ENDS_WITH)
        private String rightLike;

        @QuerySearcher(
                value = {"name"},
                searchStrategy = SearchStrategy.BOTH)
        private String bothLike;

        @QuerySorter private Boolean sort;

        @QuerySorter(
                value = "date",
                trueForAsc = false,
                enableForNull = true,
                defaultDirection = SorterDirection.DESC)
        private Boolean dateDesc;
    }

    @Test
    public void testQueryWrapper() {
        ExtractorHolder extractorHolder = new ExtractorHolder();
        extractorHolder
                .addExtractor(new FilterExtractor())
                .addExtractor(new SorterExtractor())
                .addExtractor(new SearcherExtractor());
        MyBatisFlexQueryWrapper wrapper = new MyBatisFlexQueryWrapper(extractorHolder);
        Obj1 obj1 = Obj1.builder().name("name").ageGt(30).build();
        QueryWrapper queryWrapper = wrapper.build(obj1);
        queryWrapper.from(TABLE_NAME);
        Assertions.assertEquals(
                "SELECT * FROM `table` WHERE name = 'name' AND age > 30", queryWrapper.toSQL());
        obj1 =
                Obj1.builder()
                        .name("name")
                        .ageGt(35)
                        .ageLt(40)
                        .nameNe("no")
                        .address("address")
                        .query("query")
                        .build();
        queryWrapper = wrapper.build(obj1);
        queryWrapper.from(TABLE_NAME);
        Assertions.assertEquals(
                "SELECT * FROM `table` WHERE name = 'name' AND age > 35 AND age < 40 AND name != 'no' AND ((address LIKE 'query%') OR (name LIKE 'query%'))",
                queryWrapper.toSQL());
        obj1 =
                Obj1.builder()
                        .sizeGe(100)
                        .sizeLe(200)
                        .types(List.of("t1", "t2"))
                        .noTypes(List.of("t3"))
                        .isNull(true)
                        .build();
        queryWrapper = wrapper.build(obj1);
        queryWrapper.from(TABLE_NAME);
        Assertions.assertEquals(
                "SELECT * FROM `table` WHERE size >= 100 AND size <= 200 AND type IN ('t1', 't2') AND type NOT IN ('t3') AND flag IS NULL",
                queryWrapper.toSQL().trim());
        obj1 = Obj1.builder().sizeGe(100).types(Collections.emptyList()).isNotNull(true).build();
        queryWrapper = wrapper.build(obj1);
        queryWrapper.from(TABLE_NAME);
        Assertions.assertEquals(
                "SELECT * FROM `table` WHERE size >= 100 AND flag IS NOT NULL",
                queryWrapper.toSQL().trim());
        obj1 = Obj1.builder().types(List.of("t1")).name("name").isNull(false).build();
        queryWrapper = wrapper.build(obj1);
        queryWrapper.from(TABLE_NAME);
        Assertions.assertEquals(
                "SELECT * FROM `table` WHERE name = 'name' AND type IN ('t1')",
                queryWrapper.toSQL().trim());
        obj1 = Obj1.builder().startsWith("start").endsWith("end").build();
        queryWrapper = wrapper.build(obj1);
        queryWrapper.from(TABLE_NAME);
        Assertions.assertEquals(
                "SELECT * FROM `table` WHERE name LIKE 'start%' AND name LIKE '%end'",
                queryWrapper.toSQL().trim());
        Obj2 obj2 =
                Obj2.builder()
                        .leftLike("left")
                        .rightLike("right")
                        .bothLike("both")
                        .sort(false)
                        .dateDesc(null)
                        .build();
        queryWrapper = wrapper.build(obj2);
        queryWrapper.from(TABLE_NAME);
        Assertions.assertEquals(
                "SELECT * FROM `table` WHERE (name LIKE 'left%') AND (name LIKE '%right') AND (name LIKE '%both%') ORDER BY sort DESC, date DESC",
                queryWrapper.toSQL());
        obj2 = Obj2.builder().bothLike("both").sort(true).dateDesc(true).build();
        queryWrapper = wrapper.build(obj2);
        queryWrapper.from(TABLE_NAME);
        Assertions.assertEquals(
                "SELECT * FROM `table` WHERE name LIKE '%both%' ORDER BY sort ASC, date DESC",
                queryWrapper.toSQL());
        obj2 = Obj2.builder().bothLike("both").sort(null).dateDesc(false).build();
        queryWrapper = wrapper.build(obj2);
        queryWrapper.from(TABLE_NAME);
        Assertions.assertEquals(
                "SELECT * FROM `table` WHERE name LIKE '%both%' ORDER BY date ASC",
                queryWrapper.toSQL());
    }
}
