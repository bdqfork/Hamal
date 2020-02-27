package cn.hamal.protocol.rpc.client;

import cn.hamal.core.URL;
import cn.hamal.core.constant.ProtocolProperty;
import cn.hamal.core.exception.RemoteException;
import cn.hamal.core.exception.RpcException;
import cn.hamal.rpc.protocol.client.RpcClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bdq
 * @since 2020/2/25
 */
public class NettyClient implements RpcClient {
    private volatile boolean destroyed;
    private URL url;
    private List<NettyChannel> nettyChannels;
    private AtomicInteger index = new AtomicInteger(0);

    public NettyClient(URL url) {
        this.url = url;
        initChannels(url);
    }

    private void initChannels(URL url) {
        int connections = url.getParam(ProtocolProperty.CONNECTIONS, 1);
        this.nettyChannels = new ArrayList<>(connections);
        for (int i = 0; i < connections; i++) {
            NettyChannel nettyChannel = new NettyChannel(url);
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
        NettyChannel nettyChannel = nettyChannels.get(pos % nettyChannels.size());
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
        for (NettyChannel nettyChannel : nettyChannels) {
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
            for (NettyChannel nettyChannel : nettyChannels) {
                nettyChannel.destroy();
            }
        }
    }


}
