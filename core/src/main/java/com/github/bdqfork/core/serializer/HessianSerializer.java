package com.github.bdqfork.core.serializer;

import com.github.bdqfork.core.exception.SerializerException;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Hessian的序列化实现
 *
 * @author bdq
 * @since 2020/2/26
 */
public class HessianSerializer implements Serializer {
    @Override
    public byte[] serialize(Serializable serializable) throws SerializerException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(stream);
        try {
            output.writeObject(serializable);
            output.flush();
        } catch (IOException e) {
            throw new SerializerException(e);
        }
        return stream.toByteArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws SerializerException {
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        Hessian2Input input = new Hessian2Input(stream);
        try {
            return (T) input.readObject(type);
        } catch (IOException e) {
            throw new SerializerException(e);
        }
    }
}
