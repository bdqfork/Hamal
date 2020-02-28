package cn.hamal.rpc.registry;

import cn.hamal.core.URL;
import cn.hamal.core.extension.SPI;

/**
 * @author bdq
 * @since 2020/2/24
 */
@SPI("default")
public interface RegistryFactory {
    /**
     * 根据注册中心的信息，生成对应的注册中心实例
     *
     * @param url 注册中心的信息
     * @return 注册中心实例
     */
    Registry getRegistry(URL url);
}
