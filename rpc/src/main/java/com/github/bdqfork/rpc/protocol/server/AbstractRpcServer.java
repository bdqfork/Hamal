package com.github.bdqfork.rpc.protocol.server;

import com.github.bdqfork.core.URL;

/**
 * 抽象的RpcServer，实现了基本方法
 *
 * @author bdq
 * @since 2020/2/25
 */
public abstract class AbstractRpcServer implements RpcServer {
    protected volatile boolean available;
    protected String host;
    protected Integer port;
    private URL url;

    public AbstractRpcServer(URL url) {
        this.url = url;
        this.host = url.getHost();
        this.port = url.getPort();
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void destroy() {
        available = false;
        try {
            doDestroy();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    protected abstract void doDestroy() throws Exception;
}
