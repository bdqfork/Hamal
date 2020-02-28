package com.github.bdqfork.registry;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.rpc.registry.Registry;
import com.github.bdqfork.rpc.registry.RegistryFactory;

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
