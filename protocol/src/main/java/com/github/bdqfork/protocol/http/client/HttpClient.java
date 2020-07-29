package com.github.bdqfork.protocol.http.client;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.exception.RpcException;
import com.github.bdqfork.rpc.protocol.client.RpcClient;

/**
 * @author bdq
 * @since 2020/7/28
 */
public class HttpClient implements RpcClient {
    @Override
    public Object send(Object data) throws RpcException {
        return null;
    }

    @Override
    public Object send(Object data, long timeout) throws RpcException {
        return null;
    }

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void destroy() {

    }
}
