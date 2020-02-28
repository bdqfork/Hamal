package com.github.bdqfork.rpc.registry.exporter;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;
import com.github.bdqfork.core.extension.ExtensionLoader;
import com.github.bdqfork.rpc.Invoker;
import com.github.bdqfork.rpc.registry.Notifier;
import com.github.bdqfork.rpc.registry.Registry;
import com.github.bdqfork.rpc.registry.RegistryFactory;

/**
 * Exporter实例，可以导出服务
 *
 * @author bdq
 * @since 2020/2/25
 */
public class RpcExporter implements Exporter {
    private URL url;
    private Registry registry;

    public RpcExporter(URL url) {
        this.url = url;
        initRegistry(url);
    }

    private void initRegistry(URL url) {
        RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getDefaultExtension();
        this.registry = registryFactory.getRegistry(URL.fromString(url.getParam(ProtocolProperty.REGISTRY)));
    }

    @Override
    public void export(Invoker<?> invoker) {
        URL url = invoker.getUrl();
        registry.register(url);
        if (invoker instanceof Notifier) {
            registry.subscribe(invoker.getUrl(), (Notifier) invoker);
        }
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        return registry.isAvailable();
    }

    @Override
    public void destroy() {
        if (isAvailable()) {
            registry.destroy();
        }
    }

}
