package com.github.bdqfork.rpc.registry;

import com.github.bdqfork.core.URL;

import java.util.List;

/**
 * 服务上下线通知回调
 *
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