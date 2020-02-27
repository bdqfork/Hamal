package cn.hamal.rpc.protocol.server;

import cn.hamal.rpc.Invoker;
import cn.hamal.rpc.MethodInvocation;
import cn.hamal.core.URL;

import java.lang.reflect.Method;

/**
 * @author bdq
 * @since 2020/2/25
 */
public class ServiceInvoker<T> implements Invoker<T> {
    private volatile boolean available;
    private Object instance;
    private Class<T> targetClass;
    private URL url;

    public ServiceInvoker(Object instance, Class<T> targetClass, URL url) {
        this.instance = instance;
        this.targetClass = targetClass;
        this.url = url;
    }

    @Override
    public Class<T> getInterface() {
        return targetClass;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Exception {
        Method method = targetClass.getMethod(methodInvocation.getMethodName(), methodInvocation.getArgumentTypes());
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
