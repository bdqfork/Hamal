package com.github.bdqfork.rpc.protocol.client;

import com.github.bdqfork.core.Node;
import com.github.bdqfork.core.exception.RpcException;

/**
 * 负责将客户端的数据发送到服务端，并获取响应数据
 *
 * @author bdq
 * @since 2020/2/25
 */
public interface RpcClient extends Node {

    /**
     * 发送请求
     *
     * @param data 请求数据
     * @return 响应数据
     * @throws RpcException 请求失败异常
     */
    Object send(Object data) throws RpcException;

    /**
     * 发送请求
     *
     * @param data    请求数据
     * @param timeout 超时时间
     * @return 响应数据
     * @throws RpcException 请求失败异常
     */
    Object send(Object data, long timeout) throws RpcException;

}
