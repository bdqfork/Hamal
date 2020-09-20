package com.github.bdqfork.protocol.http.codec;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import com.github.bdqfork.core.exception.SerializerException;
import com.github.bdqfork.core.serializer.JsonSerializer;
import com.github.bdqfork.core.serializer.Serializer;
import com.github.bdqfork.rpc.MethodInvocation;
import com.github.bdqfork.rpc.protocol.Request;
import com.github.bdqfork.rpc.protocol.Response;
import com.github.bdqfork.rpc.protocol.Result;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * @author h-l-j
 * @since 2020/8/9
 */
public class HttpMessageCodec extends MessageToMessageCodec<FullHttpMessage, Serializable> {
    private final Serializer serializer;

    public HttpMessageCodec(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Serializable msg, List<Object> out) throws Exception {
        if (msg instanceof Request) {
            ByteBuf context = Unpooled.wrappedBuffer(serializer.serialize(msg));
            FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/", context);
            httpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, context.readableBytes());
            out.add(httpRequest);
        }
        if (msg instanceof Response) {
            Response response = (Response) msg;
            int status = response.getStatus();
            ByteBuf context = Unpooled.wrappedBuffer(serializer.serialize(msg));
            FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(status), context);
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, context.readableBytes());
            out.add(httpResponse);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpMessage msg, List<Object> out) throws Exception {
        byte[] content = new byte[msg.content().readableBytes()];
        msg.content().getBytes(0, content, 0, content.length);
        if (msg instanceof FullHttpRequest) {
            Request request = decodeRequest(content);
            out.add(request);
        }
        if (msg instanceof FullHttpResponse) {
            Response response = decodeResponse(content);
            out.add(response);
        }
    }
    
    private Request decodeRequest(byte[] content) throws SerializerException {
        Request request = serializer.deserialize(content, Request.class);
        byte[] serialize;
        if (serializer instanceof JsonSerializer) {
            Object payload = request.getPayload();
            serialize = serializer.serialize((Serializable) payload);
            MethodInvocation methodInvocation = serializer.deserialize(serialize, MethodInvocation.class);
            Class<?>[] argType = methodInvocation.getArgumentTypes();
            Object[] args = methodInvocation.getArguments();
            for (int i = 0; i < argType.length; i++) {
                serialize = serializer.serialize((Serializable) args[i]);
                args[i] = serializer.deserialize(serialize, argType[i]);
            }
            request.setPayload(methodInvocation);
        }
        return request;
    }

    private Response decodeResponse(byte[] content) throws SerializerException {
        Response response = serializer.deserialize(content, Response.class);
        byte[] serialize;
        if (serializer instanceof JsonSerializer) {
            Object payload = response.getPayload();
            serialize = serializer.serialize((Serializable) payload);
            Result res = serializer.deserialize(serialize, Result.class);
            Object data = res.getData();
            if (data instanceof LinkedHashMap) {
                Class<?> returnType = res.getDateType();
                serialize = serializer.serialize((Serializable) data);
                res.setData(serializer.deserialize(serialize, returnType));
            }
            response.setPayload(res);
        }
        return response;
    }
    
}