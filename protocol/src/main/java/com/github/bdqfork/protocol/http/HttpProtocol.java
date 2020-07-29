package com.github.bdqfork.protocol.http;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.rpc.Invoker;
import com.github.bdqfork.rpc.protocol.AbstractProtocol;
import com.github.bdqfork.rpc.protocol.server.RpcServer;

/**
 * @author bdq
 * @since 2020/7/28
 */
public class HttpProtocol extends AbstractProtocol {
    @Override
    protected RpcServer createServer(URL url) {
        return null;
    }

    @Override
    protected <T> Invoker<T> doRefer(Class<T> serviceInterface, URL url) {
        return null;
    }
}
