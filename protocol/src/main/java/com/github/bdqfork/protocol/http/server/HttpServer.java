package com.github.bdqfork.protocol.http.server;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;
import com.github.bdqfork.core.extension.ExtensionLoader;
import com.github.bdqfork.core.serializer.Serializer;
import com.github.bdqfork.protocol.http.codec.HttpMessageCodec;
import com.github.bdqfork.protocol.rpc.server.ServerHandler;
import com.github.bdqfork.rpc.protocol.server.AbstractRpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author h-l-j
 * @since 2020/8/9
 */
public class HttpServer extends AbstractRpcServer {
    private static final Logger log = LoggerFactory.getLogger(HttpServer.class);
    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Serializer serializer;

    public HttpServer(URL url) {
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
                                .addLast(new HttpServerCodec())
                                .addLast(new HttpObjectAggregator(1048576))
                                .addLast(new HttpMessageCodec(serializer))
                                .addLast(new ServerHandler(serviceContainer));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128);

            ChannelFuture future = bootstrap.bind(host, port).sync();
            channel = future.channel();
            available = true;
            if (log.isInfoEnabled()) {
                log.info("start http server at {}:{} successful!", host, port);
            }
        } catch (Exception e) {
            destroy();
            throw e;
        }
    }

    @Override
    protected void doDestroy() {
        try {
            channel.close().sync();
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        if (log.isInfoEnabled()) {
            log.info("close http server at {}:{} successful!", host, port);
        }
    }
}
