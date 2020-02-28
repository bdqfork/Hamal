package com.github.bdqfork.protocol.rpc.client;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.rpc.protocol.client.RpcClient;
import com.github.bdqfork.rpc.protocol.client.RpcClientFactory;

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
