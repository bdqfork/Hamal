package cn.hamal.rpc.protocol.loadbalance;

import cn.hamal.rpc.protocol.client.Endpoint;

import java.util.List;
import java.util.Random;

/**
 * @author bdq
 * @since 2020/2/26
 */
public class RandomLoadBalancer extends AbstractLoadBalancer {
    @Override
    public <T> Endpoint<T> loadBalance(List<Endpoint<T>> endpoints) {
        endpoints = getAvailable(endpoints);
        if (endpoints.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(endpoints.size());
        return endpoints.get(index);
    }
}
