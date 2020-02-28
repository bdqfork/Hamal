package cn.hamal.rpc;

import cn.hamal.core.URL;
import cn.hamal.core.proxy.javassist.Proxy;
import cn.hamal.rpc.proxy.ClientProxyHandler;
import cn.hamal.rpc.protocol.server.ServiceInvoker;

/**
 * Invoke工厂类
 *
 * @author bdq
 * @since 2020/2/26
 */
public class InvokerFactory {

    /**
     * 获取一个provider端的Invoker
     *
     * @param instance         服务实例
     * @param serviceInterface 服务接口类型
     * @param url              服务URL
     * @param <T>              类型
     * @return provider端Invoker实例
     */
    public static <T> Invoker<T> getServiceInvoker(T instance, Class<T> serviceInterface, URL url) {
        return new ServiceInvoker<>(instance, serviceInterface, url);
    }

    /**
     * 根据consumer端的Invoker生成代理实例
     *
     * @param invoker consumer端的Invoker实例
     * @param <T>     类型
     * @return 代理实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Invoker<T> invoker) {
        Class<T> serviceInterface = invoker.getInterface();
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(),
                new Class[]{serviceInterface}, new ClientProxyHandler(invoker));
    }

}
