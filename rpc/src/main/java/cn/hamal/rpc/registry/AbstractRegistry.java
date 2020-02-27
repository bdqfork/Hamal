package cn.hamal.rpc.registry;


import cn.hamal.core.URL;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author bdq
 * @since 2020/2/23
 */
public abstract class AbstractRegistry implements Registry {
    protected volatile boolean available;
    protected AtomicBoolean destroyed = new AtomicBoolean(false);
    private URL url;

    public AbstractRegistry(URL url) {
        this.url = url;
        doConnect();
    }

    protected abstract void doConnect();

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void destroy() {
        if (destroyed.compareAndSet(false, true)) {
            available = false;
            doDestroy();
        }
    }

    protected abstract void doDestroy();

    @Override
    public URL getUrl() {
        return url;
    }
}
