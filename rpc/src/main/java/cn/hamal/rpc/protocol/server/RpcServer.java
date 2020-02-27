package cn.hamal.rpc.protocol.server;

import cn.hamal.core.Node;

/**
 * @author bdq
 * @since 2020/2/25
 */
public interface RpcServer extends Node {
    void start() throws Exception;
}
