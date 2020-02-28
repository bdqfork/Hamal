package cn.hamal.rpc.registry.exporter;

import cn.hamal.core.Node;
import cn.hamal.rpc.Invoker;

/**
 * 该接口的实例应该负载将服务导出，注册到注册中心
 *
 * @author bdq
 * @since 2020/2/25
 */
public interface Exporter extends Node {
    void export(Invoker<?> invoker);
}
