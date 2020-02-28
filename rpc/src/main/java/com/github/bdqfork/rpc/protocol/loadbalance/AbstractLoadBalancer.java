package com.github.bdqfork.rpc.protocol.loadbalance;

import com.github.bdqfork.core.Node;
import com.github.bdqfork.rpc.protocol.client.Endpoint;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽象负载均衡器
 *
 * @author bdq
 * @since 2020/2/26
 */
public abstract class AbstractLoadBalancer implements LoadBalancer {

    protected <T> List<Endpoint<T>> getAvailable(List<Endpoint<T>> endpoints) {
        return endpoints.stream()
                .filter(Node::isAvailable)
                .collect(Collectors.toList());
    }
}
