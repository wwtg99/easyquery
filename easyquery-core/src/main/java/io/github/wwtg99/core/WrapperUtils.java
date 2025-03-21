package io.github.wwtg99.core;


import java.util.Objects;

/**
 * Utils for wrapper.
 * @author wwtg99
 */
public class WrapperUtils {

    /**
     * Whether str contains upper case.
     * @param str string
     * @return boolean
     */
    public static boolean containUpperCase(String str) {
        if (Objects.isNull(str) || str.isEmpty()) {
            return false;
        }
        return str.matches(".*[A-Z].*");
    }

    /**
     * Transform camel case to snake case.
     * @param str camel case
     * @return snake case
     */
    public static String toSnakeCase(String str) {
        if (isStrEmpty(str)) {
            return str;
        }
        return str.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase();
    }

    /**
     * Whether string is empty or null.
     * @param str string
     * @return boolean
     */
    public static boolean isStrEmpty(String str) {
        return Objects.isNull(str) || str.isEmpty();
    }

    /**
     * Whether string is not empty or null.
     * @param str string
     * @return boolean
     */
    public static boolean isStrNotEmpty(String str) {
        return !isStrEmpty(str);
    }

}
