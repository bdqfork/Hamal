package cn.hamal.rpc.protocol.client;

import cn.hamal.core.Node;
import cn.hamal.core.exception.RpcException;

import java.util.concurrent.Future;

/**
 * 负责与远端进行通信，将Client的请求数据包装成Request，并返回请求结果
 *
 * @author bdq
 * @since 2020/2/25
 */
public interface Channel extends Node {
    /**
     * 连接服务端
     *
     * @throws Exception 连接失败异常
     */
    void connect() throws Exception;

    /**
     * 发送请求
     *
     * @param data 请求数据
     * @return 结果的future实例
     * @throws RpcException 请求异常
     */
    Future<Object> send(Object data) throws RpcException;

    /**
     * 发送请求
     *
     * @param data    请求数据
     * @param timeout 超时时间
     * @return 结果的future实例
     * @throws RpcException 请求异常
     */
    Future<Object> send(Object data, long timeout) throws RpcException;

}
