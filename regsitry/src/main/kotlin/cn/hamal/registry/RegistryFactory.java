package cn.hamal.registry;

import cn.hamal.core.URL;
import cn.hamal.core.registry.Registry;

/**
 * @author bdq
 * @since 2020/2/24
 */
public interface RegistryFactory {
    Registry getRegistry(URL url);
}
