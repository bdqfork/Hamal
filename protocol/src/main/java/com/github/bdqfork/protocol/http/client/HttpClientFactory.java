package com.github.bdqfork.protocol.http.client;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.rpc.protocol.client.RpcClient;
import com.github.bdqfork.rpc.protocol.client.RpcClientFactory;

/**
 * @author h-l-j
 * @since 2020/8/9
 */
public class HttpClientFactory implements RpcClientFactory {
    @Override
    public RpcClient getClient(URL url) {
        return new HttpClient(url);
    }
}
