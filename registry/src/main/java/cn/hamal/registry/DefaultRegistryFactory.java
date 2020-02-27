package cn.hamal.registry;

import cn.hamal.core.URL;
import cn.hamal.rpc.registry.Registry;
import cn.hamal.rpc.registry.RegistryFactory;

/**
 * @author bdq
 * @since 2020/2/26
 */
public class DefaultRegistryFactory implements RegistryFactory {
    @Override
    public Registry getRegistry(URL url) {
        return new ZookeeperRegistry(url);
    }
}
