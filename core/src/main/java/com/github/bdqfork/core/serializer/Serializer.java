package com.github.bdqfork.core.serializer;

import com.github.bdqfork.core.exception.SerializerException;
import com.github.bdqfork.core.extension.SPI;

import java.io.Serializable;

/**
 * 序列化接口
 *
 * @author bdq
 * @since 2020/2/25
 */
@SPI("hessian")
public interface Serializer {
    /**
     * 将可序列化的实例，序列化成byte[]
     *
     * @param serializable 可序列化的实例
     * @return 序列化的字节数据
     * @throws SerializerException 当序列化失败时抛出异常
     */
    byte[] serialize(Serializable serializable) throws SerializerException;

    /**
     * 将byte[]反序列化成对象
     *
     * @param bytes 字节数组
     * @param type  对象类型
     * @param <T>   泛型
     * @return 对象实例
     * @throws SerializerException 当反序列化失败时抛出异常
     */
    <T> T deserialize(byte[] bytes, Class<T> type) throws SerializerException;

}
