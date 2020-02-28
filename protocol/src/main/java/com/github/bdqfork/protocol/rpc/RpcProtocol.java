package com.github.bdqfork.protocol.rpc;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.protocol.rpc.server.NettyServer;
import com.github.bdqfork.rpc.Invoker;
import com.github.bdqfork.rpc.protocol.client.ClusterInvoker;
import com.github.bdqfork.rpc.protocol.AbstractProtocol;
import com.github.bdqfork.rpc.protocol.server.RpcServer;

/**
 * @author bdq
 * @since 2020/2/26
 */
public class RpcProtocol extends AbstractProtocol {
    @Override
    protected RpcServer createServer(URL url) {
        return new NettyServer(url);
    }

    @Override
    protected <T> Invoker<T> doRefer(Class<T> serviceInterface, URL url) {
        return new ClusterInvoker<>(serviceInterface, url);
    }

}
