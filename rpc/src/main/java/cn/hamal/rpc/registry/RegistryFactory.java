package cn.hamal.rpc.registry;

import cn.hamal.core.URL;
import cn.hamal.core.extension.SPI;

/**
 * @author bdq
 * @since 2020/2/24
 */
@SPI("default")
public interface RegistryFactory {
    Registry getRegistry(URL url);
}
