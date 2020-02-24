package com.github.hamal;

/**
 * @author bdq
 * @since 2020/2/23
 */
public interface Node {
    
    URL getUrl();

    boolean isAvailable();

    /**
     * 关闭节点
     */
    void destroy();
}