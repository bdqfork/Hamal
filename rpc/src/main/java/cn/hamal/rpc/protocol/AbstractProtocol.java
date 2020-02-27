package cn.hamal.rpc.protocol;

import cn.hamal.core.Node;
import cn.hamal.core.URL;
import cn.hamal.rpc.Invoker;
import cn.hamal.rpc.registry.exporter.Exporter;
import cn.hamal.rpc.registry.exporter.RpcExporter;
import cn.hamal.rpc.protocol.server.RpcServer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
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
