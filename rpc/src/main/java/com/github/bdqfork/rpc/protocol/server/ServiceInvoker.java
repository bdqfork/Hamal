package com.github.bdqfork.rpc.protocol.server;

import com.github.bdqfork.rpc.Invoker;
import com.github.bdqfork.rpc.MethodInvocation;
import com.github.bdqfork.core.URL;

import java.lang.reflect.Method;

/**
 * 服务执行实例
 *
 * @author bdq
 * @since 2020/2/25
 */
public class ServiceInvoker<T> implements Invoker<T> {
    /**
     * 服务状态
     */
    private volatile boolean available;
    /**
     * 服务实例
     */
    private Object instance;
    /**
     * 服务类型
     */
    private Class<T> serviceInterface;
    /**
     * 服务信息
     */
    private URL url;

    public ServiceInvoker(Object instance, Class<T> serviceInterface, URL url) {
        this.instance = instance;
        this.serviceInterface = serviceInterface;
        this.url = url;
    }

    @Override
    public Class<T> getInterface() {
        return serviceInterface;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Exception {
        Method method = serviceInterface.getMethod(methodInvocation.getMethodName(), methodInvocation.getArgumentTypes());
        return method.invoke(instance, methodInvocation.getArguments());
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void destroy() {
        available = false;
    }
}
