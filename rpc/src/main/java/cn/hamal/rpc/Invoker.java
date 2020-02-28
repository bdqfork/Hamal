package cn.hamal.rpc;


import cn.hamal.core.Node;

/**
 * 执行服务的接口
 *
 * @author bdq
 * @since 2020/2/25
 */
public interface Invoker<T> extends Node {
    /**
     * 服务接口
     */
    Class<T> getInterface();

    /**
     * 执行服务调用
     *
     * @param methodInvocation 服务信息
     * @return 调用结果
     * @throws Exception 执行失败时的异常
     */
    Object invoke(MethodInvocation methodInvocation) throws Exception;
}
