package cn.hamal.rpc.protocol.client;

import cn.hamal.core.URL;
import cn.hamal.core.constant.ProtocolProperty;
import cn.hamal.core.extension.ExtensionLoader;
import cn.hamal.core.serializer.Serializer;

/**
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
        available = false;
        doDestroy();
    }

    protected abstract void doDestroy();
}
