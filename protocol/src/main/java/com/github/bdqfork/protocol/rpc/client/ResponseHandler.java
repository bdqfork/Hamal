package com.github.bdqfork.protocol.rpc.client;

import com.github.bdqfork.rpc.DefaultFuture;
import com.github.bdqfork.rpc.protocol.Request;
import com.github.bdqfork.rpc.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author bdq
 * @since 2020/2/25
 */
public class ResponseHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Request) {
            Request request = (Request) msg;
            if (request.getEvent() == Request.HEART_BEAT) {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                if (log.isDebugEnabled()) {
                    log.debug("recieved heart beat request from {}:{}!", inetSocketAddress.getHostName(), inetSocketAddress.getPort());
                }
                Response response = new Response(request.getId());
                response.setEvent(request.getEvent());
                ctx.writeAndFlush(response);
            }
        }
        if (msg instanceof Response) {
            Response response = (Response) msg;
            DefaultFuture.received(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (log.isErrorEnabled()) {
            log.error(cause.getMessage(), cause);
        }
    }
}
