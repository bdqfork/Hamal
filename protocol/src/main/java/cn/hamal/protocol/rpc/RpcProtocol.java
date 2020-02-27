package cn.hamal.protocol.rpc;

import cn.hamal.core.URL;
import cn.hamal.protocol.rpc.server.NettyServer;
import cn.hamal.rpc.Invoker;
import cn.hamal.rpc.protocol.client.ClusterInvoker;
import cn.hamal.rpc.protocol.AbstractProtocol;
import cn.hamal.rpc.protocol.server.RpcServer;

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
