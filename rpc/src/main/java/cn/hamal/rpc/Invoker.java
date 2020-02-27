package cn.hamal.rpc;


import cn.hamal.core.Node;

/**
 * @author bdq
 * @since 2020/2/25
 */
public interface Invoker<T> extends Node {

    Class<T> getInterface();
    /**
     * 执行服务调用
     *
     * @param methodInvocation 服务信息
     * @return 调用结果
     * @throws Exception 异常
     */
    Object invoke(MethodInvocation methodInvocation) throws Exception;
}
