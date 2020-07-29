package com.github.bdqfork.protocol.rpc.codec;

import com.github.bdqfork.core.exception.SerializerException;
import com.github.bdqfork.core.exception.UnknownPacketException;
import com.github.bdqfork.core.serializer.JsonSerializer;
import com.github.bdqfork.core.serializer.Serializer;
import com.github.bdqfork.rpc.MethodInvocation;
import com.github.bdqfork.rpc.protocol.Request;
import com.github.bdqfork.rpc.protocol.Response;
import com.github.bdqfork.rpc.protocol.Result;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author bdq
 * @since 2020/2/25
 */
public class MessageCodec extends ByteToMessageCodec<Serializable> {
    private static final byte REQUEST = 0;
    private static final byte RESPONSE = 1;
    private final Serializer serializer;

    public MessageCodec(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
        out.writeByte(0x66);
        if (msg instanceof Request) {
            Request request = (Request) msg;
            doEncode(out, (Serializable) request.getPayload(), REQUEST, request.getId(),
                    request.getStatus(), request.getEvent());
        }
        if (msg instanceof Response) {
            Response response = (Response) msg;
            doEncode(out, (Serializable) response.getPayload(), RESPONSE, response.getId(),
                    response.getStatus(), response.getEvent());
        }

    }

    private void doEncode(ByteBuf out, Serializable payload, byte type, Long id, int status, byte event) throws SerializerException {
        byte[] body = serializer.serialize(payload);
        out.writeInt(body.length);
        out.writeByte(type);
        out.writeLong(id);
        out.writeInt(status);
        out.writeByte(event);
        out.writeBytes(body);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte magic = in.readByte();
        if (magic != 0x66) {
            throw new UnknownPacketException();
        }
        int len = in.readInt();
        byte type = in.readByte();
        long id = in.readLong();
        int status = in.readInt();
        byte event = in.readByte();
        byte[] body = new byte[len];
        in.readBytes(body);
        if (REQUEST == type) {
            Request request = new Request(id);
            request.setStatus(status);
            request.setEvent(event);
            MethodInvocation methodInvocation = decodeMethodInvocation(body);
            request.setPayload(methodInvocation);
            out.add(request);
        }
        if (RESPONSE == type) {
            Response response = new Response(id);
            response.setStatus(status);
            response.setEvent(event);
            Result result = decodeResult(body);
            response.setPayload(result);
            out.add(response);
        }
    }

    private MethodInvocation decodeMethodInvocation(byte[] body) throws SerializerException {
        MethodInvocation methodInvocation = serializer.deserialize(body, MethodInvocation.class);
        if (serializer instanceof JsonSerializer) {
            Class<?>[] argType = methodInvocation.getArgumentTypes();
            Object[] args = methodInvocation.getArguments();
            for (int i = 0; i < argType.length; i++) {
                byte[] serialize = serializer.serialize((Serializable) args[i]);
                args[i] = serializer.deserialize(serialize, argType[i]);
            }
        }
        return methodInvocation;
    }

    private Result decodeResult(byte[] body) throws SerializerException {
        Result res = serializer.deserialize(body, Result.class);
        if (serializer instanceof JsonSerializer) {
            Object data = res.getData();
            if (data instanceof LinkedHashMap) {
                Class<?> returnType = res.getDateType();
                byte[] serialize = serializer.serialize((Serializable) data);
                res.setData(serializer.deserialize(serialize, returnType));
            }
        }
        return res;
    }
}
