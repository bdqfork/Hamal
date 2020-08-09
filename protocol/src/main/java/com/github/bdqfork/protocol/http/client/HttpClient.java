package com.github.bdqfork.protocol.http.client;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;
import com.github.bdqfork.core.exception.RemoteException;
import com.github.bdqfork.core.exception.RpcException;
import com.github.bdqfork.rpc.protocol.client.RpcClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author h-l-j
 * @since 2020/8/9
 */
public class HttpClient implements RpcClient {
    private volatile boolean destroyed;
    private URL url;
    private List<HttpNettyChannel> nettyChannels;
    private AtomicInteger index = new AtomicInteger(0);

    public HttpClient(URL url) {
        this.url = url;
        initChannels(url);
    }

    private void initChannels(URL url) {
        int connections = url.getParam(ProtocolProperty.CONNECTIONS, 1);
        this.nettyChannels = new ArrayList<>(connections);
        for (int i = 0; i < connections; i++) {
            HttpNettyChannel nettyChannel = new HttpNettyChannel(url);
            nettyChannels.add(nettyChannel);
        }
    }

    @Override
    public Object send(Object data) throws RpcException {
        return send(data, ProtocolProperty.DEFAULT_TIMEOUT);
    }

    @Override
    public Object send(Object data, long timeout) throws RpcException {
        int pos = index.getAndIncrement();
        HttpNettyChannel nettyChannel = nettyChannels.get(pos % nettyChannels.size());
        try {
            return nettyChannel.send(data, timeout).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RemoteException(e);
        }
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        if (destroyed) {
            return false;
        }
        for (HttpNettyChannel nettyChannel : nettyChannels) {
            if (nettyChannel.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        if (!destroyed) {
            destroyed = true;
            for (HttpNettyChannel nettyChannel : nettyChannels) {
                nettyChannel.destroy();
            }
        }
    }


}
