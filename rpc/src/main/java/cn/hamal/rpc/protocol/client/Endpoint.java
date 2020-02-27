package cn.hamal.rpc.protocol.client;

import cn.hamal.rpc.Invoker;

import java.net.InetSocketAddress;

/**
 * @author bdq
 * @since 2020/2/25
 */
public interface Endpoint<T> extends Invoker<T> {

    InetSocketAddress getAddress();

    int getWeight();
}
