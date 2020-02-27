package cn.hamal.rpc;

import cn.hamal.core.URL;
import cn.hamal.core.proxy.javassist.Proxy;
import cn.hamal.rpc.proxy.ClientProxyHandler;
import cn.hamal.rpc.protocol.server.ServiceInvoker;

/**
 * @author bdq
 * @since 2020/2/26
 */
public class InvokerFactory {

    public static <T> ServiceInvoker<T> getServiceInvoker(T instance, Class<T> serviceInterface, URL url) {
        return new ServiceInvoker<>(instance, serviceInterface, url);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Invoker<T> invoker) {
        Class<T> serviceInterface = invoker.getInterface();
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(),
                new Class[]{serviceInterface}, new ClientProxyHandler(invoker));
    }

}
