package com.github.bdqfork.rpc.protocol.loadbalance;

import com.github.bdqfork.core.extension.SPI;
import com.github.bdqfork.rpc.protocol.client.Endpoint;

import java.util.List;

/**
 * 负载均衡器
 *
 * @author bdq
 * @since 2020/2/25
 */
@SPI("random")
public interface LoadBalancer {
    /**
     * 负载均衡策略
     *
     * @param endpoints 服务端点
     * @param <T>       服务类型
     * @return 负载均衡的结果
     */
    <T> Endpoint<T> loadBalance(List<Endpoint<T>> endpoints);
}
