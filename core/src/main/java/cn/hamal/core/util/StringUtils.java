package cn.hamal.core.util;

/**
 * @author bdq
 * @since 2020/2/26
 */
public class StringUtils {
    /**
     * 检测字符串是否为null或者""
     *
     * @param str 待检测字符串
     * @return 是否为null或者""
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }
}
