package cn.hamal.rpc.proxy;

import cn.hamal.rpc.Invoker;
import cn.hamal.rpc.MethodInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author bdq
 * @since 2020/2/26
 */
public class ClientProxyHandler implements InvocationHandler {
    private Invoker<?> invoker;

    public ClientProxyHandler(Invoker<?> invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("toString".equals(method.getName())) {
            return invoker.toString();
        }
        if ("hashCode".equals(method.getName())) {
            return invoker.hashCode();
        }
        if ("equals".equals(method.getName())) {
            return invoker.equals(args[0]);
        }
        MethodInvocation methodInvocation = new MethodInvocation(invoker.getInterface().getCanonicalName(), method.getName(),
                method.getParameterTypes(), args);
        return invoker.invoke(methodInvocation);
    }
}
