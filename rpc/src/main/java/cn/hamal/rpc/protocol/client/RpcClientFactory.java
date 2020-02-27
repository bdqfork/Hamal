package cn.hamal.rpc.protocol.client;

import cn.hamal.core.URL;
import cn.hamal.core.extension.SPI;

/**
 * @author bdq
 * @since 2020/2/25
 */
@SPI("rpc")
public interface RpcClientFactory {
    RpcClient getClient(URL url);
}
