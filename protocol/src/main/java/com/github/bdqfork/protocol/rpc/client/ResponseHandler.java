package com.github.bdqfork.protocol.rpc.client;

import com.github.bdqfork.rpc.DefaultFuture;
import com.github.bdqfork.rpc.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author bdq
 * @since 2020/2/25
 */
public class ResponseHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = (Response) msg;
        DefaultFuture.received(response);
    }
}
