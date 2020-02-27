package cn.hamal.rpc.protocol.client;

import cn.hamal.core.URL;
import cn.hamal.core.constant.ProtocolProperty;
import cn.hamal.core.constant.RequestType;
import cn.hamal.core.extension.ExtensionLoader;
import cn.hamal.rpc.MethodInvocation;
import cn.hamal.rpc.Request;
import cn.hamal.rpc.RpcContext;

import java.net.InetSocketAddress;

/**
 * @author bdq
 * @since 2020/2/25
 */
public class RpcInvoker<T> implements Endpoint<T> {
    private URL url;
    private int weight;
    private RpcClient rpcClient;
    private InetSocketAddress socketAddress;
    private Class<T> serviceInterface;
    private volatile boolean destroyed;

    public RpcInvoker(Class<T> serviceInterface, URL url) {
        this.serviceInterface = serviceInterface;
        this.url = url;
        initWeight(url);
        initAddress(url);
        initClient(url);
    }

    private void initWeight(URL url) {
        this.weight = url.getParam(ProtocolProperty.WEIGHT, 0);
    }

    private void initAddress(URL url) {
        this.socketAddress = new InetSocketAddress(url.getHost(), url.getPort());
    }

    private void initClient(URL url) {
        ExtensionLoader<RpcClientFactory> extensionLoader = ExtensionLoader.getExtensionLoader(RpcClientFactory.class);
        String protocol = url.getParam(ProtocolProperty.PROTOCOL);
        RpcClientFactory rpcClientFactory = extensionLoader.getExtension(protocol);
        rpcClient = rpcClientFactory.getClient(url);
    }

    @Override
    public Class<T> getInterface() {
        return serviceInterface;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Exception {
        RpcContext rpcContext = RpcContext.getContext();
        rpcContext.setUrl(url);
        rpcContext.setMethodInvocation(methodInvocation);

        Request request = new Request(Request.newId(), RequestType.INVOKE);
        request.setPayload(methodInvocation);

        rpcContext.setRequest(request);

        Long timeout = url.getParam(ProtocolProperty.TIMEOUT, ProtocolProperty.DEFAULT_TIMEOUT);
        Object result = rpcClient.send(request, timeout);

        //clear，防止内存泄漏
        RpcContext.remove();
        return result;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        return rpcClient.isAvailable();
    }

    @Override
    public void destroy() {
        if (!destroyed) {
            destroyed = true;
            rpcClient.destroy();
        }
    }

    @Override
    public InetSocketAddress getAddress() {
        return socketAddress;
    }

    @Override
    public int getWeight() {
        return weight;
    }
}
