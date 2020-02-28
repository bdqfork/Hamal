package com.github.bdqfork.rpc;

import com.github.bdqfork.core.URL;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc上下文，记录了上下文信息，在请求完成之后会被销毁。
 *
 * @author bdq
 * @since 2020/2/25
 */
public class RpcContext {
    private static ThreadLocal<RpcContext> threadLocal = ThreadLocal.withInitial(RpcContext::new);
    /**
     * 消费者url
     */
    private URL consumer;
    /**
     * 提供者url
     */
    private URL provider;
    /**
     * 请求参数
     */
    private Request request;
    /**
     * 响应参数
     */
    private Response response;
    /**
     * 请求future
     */
    private DefaultFuture future;
    /**
     * 上下文参数
     */
    private Map<String, Object> data;

    private RpcContext() {
        data = new HashMap<>();
    }

    /**
     * 获取一个上下文实例，上下文实现与当前线程绑定
     *
     * @return 上下文实例
     */
    public static RpcContext getContext() {
        return threadLocal.get();
    }

    /**
     * 移除一个上下文
     */
    public static void remove() {
        threadLocal.remove();
    }

    public URL getConsumer() {
        return consumer;
    }

    public void setConsumer(URL consumer) {
        this.consumer = consumer;
    }

    public URL getProvider() {
        return provider;
    }

    public void setProvider(URL provider) {
        this.provider = provider;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public DefaultFuture getFuture() {
        return future;
    }

    public void setFuture(DefaultFuture future) {
        this.future = future;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
