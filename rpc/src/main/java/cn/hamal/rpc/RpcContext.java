package cn.hamal.rpc;

import cn.hamal.core.URL;

import java.util.concurrent.CompletableFuture;

/**
 * @author bdq
 * @since 2020/2/25
 */
public class RpcContext {
    private static ThreadLocal<RpcContext> threadLocal = ThreadLocal.withInitial(RpcContext::new);
    private URL url;
    private MethodInvocation methodInvocation;
    private Request request;
    private Response response;
    private CompletableFuture<Object> future;

    public static RpcContext getContext() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public MethodInvocation getMethodInvocation() {
        return methodInvocation;
    }

    public void setMethodInvocation(MethodInvocation methodInvocation) {
        this.methodInvocation = methodInvocation;
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

    public CompletableFuture<Object> getFuture() {
        return future;
    }

    public void setFuture(CompletableFuture<Object> future) {
        this.future = future;
    }
}
