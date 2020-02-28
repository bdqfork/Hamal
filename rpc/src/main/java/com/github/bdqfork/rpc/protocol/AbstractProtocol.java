package com.github.bdqfork.rpc.protocol;

import com.github.bdqfork.core.Node;
import com.github.bdqfork.core.URL;
import com.github.bdqfork.rpc.Invoker;
import com.github.bdqfork.rpc.registry.exporter.Exporter;
import com.github.bdqfork.rpc.registry.exporter.RpcExporter;
import com.github.bdqfork.rpc.protocol.server.RpcServer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 抽象的Protocol，实现了基本的方法
 *
 * @author bdq
 * @since 2020/2/26
 */
public abstract class AbstractProtocol implements Protocol {
    private List<Invoker<?>> invokers = new CopyOnWriteArrayList<>();
    protected RpcServer rpcServer;

    @Override
    public void open(URL url) throws Exception {
        rpcServer = createServer(url);
        rpcServer.start();
    }

    protected abstract RpcServer createServer(URL url);

    @Override
    public Exporter export(URL url) {
        return new RpcExporter(url);
    }

    @Override
    public <T> Invoker<T> refer(Class<T> serviceInterface, URL url) {
        Invoker<T> invoker = doRefer(serviceInterface, url);
        invokers.add(invoker);
        return invoker;
    }

    protected abstract <T> Invoker<T> doRefer(Class<T> serviceInterface, URL url);

    @Override
    public void destroy() {
        if (rpcServer != null) {
            rpcServer.destroy();
        }
        invokers.forEach(Node::destroy);
    }
}
