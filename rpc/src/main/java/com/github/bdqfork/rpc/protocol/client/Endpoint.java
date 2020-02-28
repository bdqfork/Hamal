package com.github.bdqfork.rpc.protocol.client;

import com.github.bdqfork.rpc.Invoker;

import java.net.InetSocketAddress;

/**
 * 服务端点
 *
 * @author bdq
 * @since 2020/2/25
 */
public interface Endpoint<T> extends Invoker<T> {
    /**
     * 返回服务地址
     *
     * @return 服务地址
     */
    InetSocketAddress getAddress();

    /**
     * 获取服务权重
     *
     * @return 服务权重
     */
    int getWeight();
}
