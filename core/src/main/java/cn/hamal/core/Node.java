package cn.hamal.core;

/**
 * @author bdq
 * @since 2020/2/23
 */
public interface Node {

    /**
     * 获取节点url信息
     *
     * @return url信息
     */
    URL getUrl();

    /**
     * @return 是否可用
     */
    boolean isAvailable();

    /**
     * 关闭节点
     */
    void destroy();
}