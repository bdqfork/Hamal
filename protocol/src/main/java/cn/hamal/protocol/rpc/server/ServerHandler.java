package cn.hamal.protocol.rpc.server;

import cn.hamal.core.extension.ExtensionLoader;
import cn.hamal.rpc.Invoker;
import cn.hamal.rpc.MethodInvocation;
import cn.hamal.rpc.Request;
import cn.hamal.rpc.Response;
import cn.hamal.rpc.container.ServiceContainer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bdq
 * @since 2020/2/25
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);
    private ServiceContainer serviceContainer = ExtensionLoader.getExtensionLoader(ServiceContainer.class)
            .getDefaultExtension();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) msg;
        if (log.isDebugEnabled()) {
            log.debug("recieved request {}!", request.getId());
        }

        Response response = new Response(request.getId());
        response.setEvent(request.getEvent());

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
