package com.github.bdqfork.rpc.protocol.server;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.extension.SPI;

/**
 * 服务工厂
 *
 * @author bdq
 * @since 2020/2/25
 */
@SPI("netty")
public interface ServerFactory {
    /**
     * 获取一个rpc服务器实例
     *
     * @param url 服务器描述
     * @return 服务器实例
     */
    RpcServer getServer(URL url);
}
