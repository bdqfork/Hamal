package com.github.bdqfork.rpc.protocol.loadbalance;

import com.github.bdqfork.rpc.protocol.client.Endpoint;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡
 *
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
