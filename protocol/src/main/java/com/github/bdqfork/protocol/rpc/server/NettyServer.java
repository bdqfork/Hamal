package com.github.bdqfork.protocol.rpc.server;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;
import com.github.bdqfork.core.extension.ExtensionLoader;
import com.github.bdqfork.core.serializer.Serializer;
import com.github.bdqfork.protocol.rpc.codec.MessageCodec;
import com.github.bdqfork.rpc.protocol.server.AbstractRpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bdq
 * @since 2020/2/25
 */
public class NettyServer extends AbstractRpcServer {
    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);
    private Channel channel;
    private Serializer serializer;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public NettyServer(URL url) {
        super(url);
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        initSerializer(url);
    }

    private void initSerializer(URL url) {
        String serializer = url.getParam(ProtocolProperty.SERIALIZER, ProtocolProperty.DEFAULT_SERIALIZER);
        this.serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(serializer);
    }

    @Override
    public void start() throws Exception {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 1, 4, 14, 0))
                                    .addLast(new MessageCodec(serializer))
                                    .addLast(new ServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(host, port).sync();
            channel = future.channel();
            available = true;
        } catch (Exception e) {
            destroy();
            throw e;
        }
    }

    @Override
    protected void doDestroy() {
        channel.close();
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
