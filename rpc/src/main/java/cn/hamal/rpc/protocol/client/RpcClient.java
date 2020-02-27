package cn.hamal.rpc.protocol.client;

import cn.hamal.core.Node;
import cn.hamal.core.exception.RpcException;

/**
 * 负责将客户端的数据发送到服务端
 *
 * @author bdq
 * @since 2020/2/25
 */
public interface RpcClient extends Node {

    Object send(Object data) throws RpcException;

    Object send(Object data, long timeout) throws RpcException;

}
