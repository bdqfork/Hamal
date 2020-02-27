package cn.hamal.protocol.rpc.client;

import cn.hamal.rpc.DefaultFuture;
import cn.hamal.rpc.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
