package com.github.bdqfork.protocol.http.client;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;
import com.github.bdqfork.core.exception.RemoteException;
import com.github.bdqfork.core.exception.RpcException;
import com.github.bdqfork.protocol.http.codec.HttpMessageCodec;
import com.github.bdqfork.protocol.rpc.client.ResponseHandler;
import com.github.bdqfork.rpc.DefaultFuture;
import com.github.bdqfork.rpc.protocol.Request;
import com.github.bdqfork.rpc.RpcContext;
import com.github.bdqfork.rpc.protocol.client.AbstractChannel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

/**
 * @author h-l-j
 * @since 2020/8/9
 */
public class HttpNettyChannel extends AbstractChannel {
    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);
    private volatile Channel channel;
    private long timeout;
    private Bootstrap bootstrap;
    private EventLoopGroup workerGroup;

    public HttpNettyChannel(URL url) {
        super(url);
        timeout = url.getParam(ProtocolProperty.TIMEOUT, 1000L);
        workerGroup = new NioEventLoopGroup(1);
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(host, port)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                            .addLast(new HttpClientCodec())
                            .addLast(new HttpObjectAggregator(1048576))
                            .addLast(new HttpMessageCodec(serializer))
                            .addLast(new ResponseHandler());
                    }
                });
    }

    @Override
    public void connect() throws RemoteException {
        if (channel != null && channel.isActive()) {
            return;
        }
        synchronized (this) {
            ChannelFuture channelFuture = bootstrap.connect();
            boolean result = channelFuture.awaitUninterruptibly(timeout);
            if (result && channelFuture.isSuccess()) {
                Channel oldChannel = channel;
                channel = channelFuture.channel();
                if (oldChannel != null) {
                    oldChannel.close();
                }
                if (!available) {
                    channel.close();
                }
            } else {
                throw new RemoteException(channelFuture.cause());
            }
            if (log.isDebugEnabled()) {
                log.debug("connect to {}:{} successful!", host, port);
            }
        }
    }

    @Override
    public Future<Object> send(Object data) throws RpcException {
        return send(data, ProtocolProperty.DEFAULT_TIMEOUT);
    }

    @Override
    public Future<Object> send(Object data, long timeout) throws RpcException {
        connect();
        if (!available) {
            throw new RemoteException("Failed to send request " + data + ", cause: The channel " + this + " is closed!");
        }
        Request request = (Request) data;
        ChannelFuture future = channel.writeAndFlush(request);
        boolean result = future.awaitUninterruptibly(timeout);

        if (result && future.isSuccess()) {
            if (log.isDebugEnabled()) {
                log.debug("send message success !");
            }

            DefaultFuture defaultFuture = DefaultFuture.newDefaultFuture(request.getId(), timeout);

            RpcContext rpcContext = RpcContext.getContext();
            rpcContext.setFuture(defaultFuture);

            return defaultFuture;
        } else {
            throw new RpcException(future.cause());
        }
    }

    @Override
    protected void doDestroy() {
        if (channel != null) {
            channel.close();
        }
        workerGroup.shutdownGracefully();
        if (log.isInfoEnabled()) {
            log.info("closed connection {}:{}!", host, port);
        }
    }

    @Override
    public String toString() {
        return "NettyChannel{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
