package com.github.bdqfork.rpc.protocol.client;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.extension.SPI;

/**
 * RpcClient工厂
 *
 * @author bdq
 * @since 2020/2/25
 */
@SPI("rpc")
public interface RpcClientFactory {
    /**
     * 根据客户端信息创建一个RpcClient实例
     *
     * @param url 客户端信息
     * @return RpcClient实例
     */
    RpcClient getClient(URL url);
}
