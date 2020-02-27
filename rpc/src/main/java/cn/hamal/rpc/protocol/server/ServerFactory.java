package cn.hamal.rpc.protocol.server;

import cn.hamal.core.URL;
import cn.hamal.core.extension.SPI;

/**
 * @author bdq
 * @since 2020/2/25
 */
@SPI("netty")
public interface ServerFactory {
    RpcServer getServer(URL url);
}
