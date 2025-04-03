package io.github.wwtg99.core;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("WrapperUtils Test")
public class WrapperUtilsTest {

    @Data
    @AllArgsConstructor
    static class StrCase {
        private String key;
        private Boolean result;
    }

    @Data
    @AllArgsConstructor
    static class TransformCase {
        private String original;
        private Boolean containUpper;
        private String parsed;
    }

    @Test
    public void testStrEmpty() {
        List<StrCase> cases =
                List.of(
                        new StrCase("", true),
                        new StrCase(null, true),
                        new StrCase(" ", false),
                        new StrCase("a", false),
                        new StrCase("1", false),
                        new StrCase(" ab ", false),
                        new StrCase("\n", false));
        for (StrCase sc : cases) {
            boolean ret = WrapperUtils.isStrEmpty(sc.getKey());
            boolean ret2 = WrapperUtils.isStrNotEmpty(sc.getKey());
            Assertions.assertEquals(sc.getResult(), ret);
            Assertions.assertEquals(ret, !ret2);
        }
    }

    @Test
    public void testTransformCase() {
        List<TransformCase> cases =
                List.of(
                        new TransformCase(null, false, null),
                        new TransformCase("", false, ""),
                        new TransformCase("a", false, "a"),
                        new TransformCase("a1", false, "a1"),
                        new TransformCase("A", true, "a"),
                        new TransformCase("ABC", true, "abc"),
                        new TransformCase("Abc", true, "abc"),
                        new TransformCase("abcd", false, "abcd"),
                        new TransformCase("A123", true, "a123"),
                        new TransformCase("AbcDef", true, "abc_def"),
                        new TransformCase("abcDefGh", true, "abc_def_gh"),
                        new TransformCase("ab3G", true, "ab3_g"),
                        new TransformCase("ab3Gh", true, "ab3_gh"),
                        new TransformCase("a2bGh", true, "a2b_gh"));
        for (TransformCase sc : cases) {
            boolean ret = WrapperUtils.containUpperCase(sc.getOriginal());
            Assertions.assertEquals(sc.getContainUpper(), ret);
            String parsed = WrapperUtils.toSnakeCase(sc.getOriginal());
            Assertions.assertEquals(sc.getParsed(), parsed);
        }
    }
}
