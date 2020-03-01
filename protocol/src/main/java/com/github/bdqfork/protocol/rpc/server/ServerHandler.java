package com.github.bdqfork.protocol.rpc.server;

import com.github.bdqfork.rpc.*;
import com.github.bdqfork.rpc.container.ServiceContainer;
import com.github.bdqfork.rpc.protocol.Request;
import com.github.bdqfork.rpc.protocol.Response;
import com.github.bdqfork.rpc.protocol.Result;
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
    private ServiceContainer serviceContainer;

    public ServerHandler(ServiceContainer serviceContainer) {
        this.serviceContainer = serviceContainer;
    }

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
            response.setPayload(new Result(methodInvocation.getReturnType(), result));
        } catch (Exception e) {
            response.setStatus(Request.ERROR);
            response.setPayload(new Result(e));
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        }

        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        if (log.isErrorEnabled()) {
            log.error(cause.getMessage(), cause);
        }
    }
}
