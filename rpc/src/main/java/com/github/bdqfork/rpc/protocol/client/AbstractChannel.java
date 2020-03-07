package com.github.bdqfork.rpc.protocol.client;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;
import com.github.bdqfork.core.extension.ExtensionLoader;
import com.github.bdqfork.core.serializer.Serializer;

/**
 * 抽象Channel
 *
 * @author bdq
 * @since 2020/2/25
 */
public abstract class AbstractChannel implements Channel {
    protected volatile boolean available = true;
    protected String host;
    protected Integer port;
    protected Serializer serializer;
    private URL url;

    public AbstractChannel(URL url) {
        this.url = url;
        this.host = url.getHost();
        this.port = url.getPort();
        initSerializer(url);
    }

    private void initSerializer(URL url) {
        String serializerType = url.getParam(ProtocolProperty.SERIALIZER);
        this.serializer = ExtensionLoader.getExtensionLoader(Serializer.class)
                .getExtension(serializerType);
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
        doDestroy();
        available = false;
    }

    protected abstract void doDestroy();
}
