package cn.hamal.rpc.protocol.loadbalance;

import cn.hamal.core.Node;
import cn.hamal.rpc.protocol.client.Endpoint;

import java.util.List;
import java.util.stream.Collectors;

/**
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
