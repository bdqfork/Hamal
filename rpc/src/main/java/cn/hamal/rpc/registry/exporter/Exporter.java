package cn.hamal.rpc.registry.exporter;

import cn.hamal.core.Node;
import cn.hamal.rpc.Invoker;

/**
 * @author bdq
 * @since 2020/2/25
 */
public interface Exporter extends Node {
    void export(Invoker<?> invoker);
}
