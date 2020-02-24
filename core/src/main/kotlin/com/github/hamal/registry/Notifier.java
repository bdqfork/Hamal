package com.github.hamal.registry;

import com.github.hamal.URL;

import java.util.List;

/**
 * @author bdq
 * @since 2020/2/23
 */
public interface Notifier {
    /**
     * 通知方法
     *
     * @param urls 可获取的服务列表
     */
    void notify(List<URL> urls);
}