package cn.hamal.protocol.rpc.server;

import cn.hamal.core.extension.ExtensionLoader;
import cn.hamal.rpc.Invoker;
import cn.hamal.rpc.MethodInvocation;
import cn.hamal.rpc.Request;
import cn.hamal.rpc.Response;
import cn.hamal.rpc.container.ServiceContainer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author bdq
 * @since 2020/2/25
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private ServiceContainer serviceContainer = ExtensionLoader.getExtensionLoader(ServiceContainer.class)
            .getDefaultExtension();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) msg;

        Response response = new Response(request.getId(), request.getEvent());

        MethodInvocation methodInvocation = (MethodInvocation) request.getPayload();

        Invoker<?> invoker = serviceContainer.get(methodInvocation.getInterfaceName());
        try {
            Object result = invoker.invoke(methodInvocation);
            response.setPayload(result);
        } catch (Exception e) {
            response.setStatus(Request.ERROR);
            response.setPayload(e);
        }

        ctx.writeAndFlush(response);
    }

}
