package com.github.bdqfork.protocol.rpc.client;

import com.github.bdqfork.rpc.DefaultFuture;
import com.github.bdqfork.rpc.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bdq
 * @since 2020/2/25
 */
public class ResponseHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = (Response) msg;
        DefaultFuture.received(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        if (log.isErrorEnabled()) {
            log.error(cause.getMessage(), cause);
        }
    }
}
