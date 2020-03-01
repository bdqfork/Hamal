package com.github.bdqfork.protocol.rpc.server;

import com.github.bdqfork.rpc.Invoker;
import com.github.bdqfork.rpc.MethodInvocation;
import com.github.bdqfork.rpc.container.ServiceContainer;
import com.github.bdqfork.rpc.protocol.Request;
import com.github.bdqfork.rpc.protocol.Response;
import com.github.bdqfork.rpc.protocol.Result;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

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
        if (msg instanceof Response) {
            Response response = (Response) msg;
            if (response.getEvent() == Request.HEART_BEAT) {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                if (log.isDebugEnabled()) {
                    log.debug("recieved heart beat response from {}:{}!", address.getHostName(), address.getPort());
                }
            }
        }
        if (msg instanceof Request) {
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
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            IdleState idleState = event.state();
            if (idleState == IdleState.READER_IDLE) {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                if (log.isInfoEnabled()) {
                    log.info("server closing connection {}:{}!", address.getHostName(), address.getPort());
                }
                ctx.close();
            }
            if (idleState == IdleState.WRITER_IDLE) {
                Request request = new Request(Request.newId());
                request.setEvent(Request.HEART_BEAT);
                ctx.writeAndFlush(request);
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                if (log.isDebugEnabled()) {
                    log.debug("send heart beat request to {}:{}!", address.getHostName(), address.getPort());
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (log.isErrorEnabled()) {
            log.error(cause.getMessage(), cause);
        }
    }
}
