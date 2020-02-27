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

    void connect() throws Exception;

    Future<Object> send(Object data) throws RpcException;

    Future<Object> send(Object data, long timeout) throws RpcException;

}
