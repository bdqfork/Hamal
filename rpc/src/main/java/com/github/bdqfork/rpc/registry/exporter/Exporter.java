package com.github.bdqfork.rpc.registry.exporter;

import com.github.bdqfork.core.Node;
import com.github.bdqfork.rpc.Invoker;

/**
 * 该接口的实例应该负载将服务导出，注册到注册中心
 *
 * @author bdq
 * @since 2020/2/25
 */
public interface Exporter extends Node {
    /**
     * 导出服务
     *
     * @param invoker 消费者或者服务提供者
     */
    void export(Invoker<?> invoker);
}
