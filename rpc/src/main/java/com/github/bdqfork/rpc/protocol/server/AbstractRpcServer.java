package com.github.bdqfork.rpc.protocol.server;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;
import com.github.bdqfork.core.extension.ExtensionLoader;
import com.github.bdqfork.rpc.container.ServiceContainer;

/**
 * 抽象的RpcServer，实现了基本方法
 *
 * @author bdq
 * @since 2020/2/25
 */
public abstract class AbstractRpcServer implements RpcServer {
    protected volatile boolean available;
    protected ServiceContainer serviceContainer;
    protected String host;
    protected Integer port;
    private URL url;

    public AbstractRpcServer(URL url) {
        this.url = url;
        this.host = url.getHost();
        this.port = url.getPort();
        initServiceContainer(url);
    }

    private void initServiceContainer(URL url) {
        String containerType = url.getParam(ProtocolProperty.CONTATINER, ProtocolProperty.DEFAULT_CONTAINER);
        this.serviceContainer = ExtensionLoader.getExtensionLoader(ServiceContainer.class).getExtension(containerType);
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
        try {
            doDestroy();
            available = false;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    protected abstract void doDestroy() throws Exception;
}
