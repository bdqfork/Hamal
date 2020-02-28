package cn.hamal.rpc.protocol.server;

import cn.hamal.core.Node;

/**
 * rpc服务器
 *
 * @author bdq
 * @since 2020/2/25
 */
public interface RpcServer extends Node {
    /**
     * 启动服务器
     *
     * @throws Exception 启动异常时抛出
     */
    void start() throws Exception;
}
