package com.github.bdqfork.rpc.container;

import com.github.bdqfork.core.exception.ConfilictServiceException;
import com.github.bdqfork.rpc.Invoker;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的服务容器
 *
 * @author bdq
 * @since 2020/2/26
 */
public class DefaultServiceContainer implements ServiceContainer {
    /**
     * service容器
     */
    private final Map<String, Invoker<?>> serviceInvokerMap = new ConcurrentHashMap<>(256);

    @Override
    public void regsiter(String serviceName, Invoker<?> invoker) throws ConfilictServiceException {
        if (serviceInvokerMap.containsKey(serviceName)) {
            throw new ConfilictServiceException(String.format("conflict service %s!", serviceName));
        }
        serviceInvokerMap.put(serviceName, invoker);
    }

    @Override
    public void remove(String serviceName) {
        serviceInvokerMap.remove(serviceName);
    }

    @Override
    public Invoker<?> get(String serviceName) {
        return serviceInvokerMap.get(serviceName);
    }

    @Override
    public Map<String, Invoker<?>> getAll() {
        return Collections.unmodifiableMap(serviceInvokerMap);
    }

    @Override
    public int size() {
        return serviceInvokerMap.size();
    }
}
