package cn.hamal.rpc.protocol.loadbalance;

import cn.hamal.core.extension.SPI;
import cn.hamal.rpc.protocol.client.Endpoint;

import java.util.List;

/**
 * @author bdq
 * @since 2020/2/25
 */
@SPI("random")
public interface LoadBalancer {
   <T> Endpoint<T> loadBalance(List<Endpoint<T>> endpoints);
}
