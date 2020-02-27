package cn.hamal.protocol.rpc.client;

import cn.hamal.core.URL;
import cn.hamal.rpc.protocol.client.RpcClient;
import cn.hamal.rpc.protocol.client.RpcClientFactory;

/**
 * @author bdq
 * @since 2020/2/26
 */
public class NettyClientFactory implements RpcClientFactory {
    @Override
    public RpcClient getClient(URL url) {
        return new NettyClient(url);
    }
}
