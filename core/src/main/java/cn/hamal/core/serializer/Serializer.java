package cn.hamal.core.serializer;

import cn.hamal.core.exception.SerializerException;
import cn.hamal.core.extension.SPI;

import java.io.Serializable;

/**
 * @author bdq
 * @since 2020/2/25
 */
@SPI("hessian")
public interface Serializer {

    byte[] serialize(Serializable serializable) throws SerializerException;

    <T> T deserialize(byte[] bytes, Class<T> type) throws SerializerException;

}
