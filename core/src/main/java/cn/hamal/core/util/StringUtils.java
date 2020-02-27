package cn.hamal.core.util;

/**
 * @author bdq
 * @since 2020/2/26
 */
public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }
}
