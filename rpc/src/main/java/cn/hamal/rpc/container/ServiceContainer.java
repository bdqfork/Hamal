package cn.hamal.rpc.container;

import cn.hamal.core.exception.ConfilictServiceException;
import cn.hamal.core.extension.SPI;
import cn.hamal.rpc.Invoker;

import java.util.Map;

/**
 * @author bdq
 * @since 2020/2/26
 */
@SPI("rpc")
public interface ServiceContainer {

    void regsiter(String serviceName, Invoker<?> invoker) throws ConfilictServiceException;

    void remove(String serviceName);

    Invoker<?> get(String serviceName);

    Map<String, Invoker<?>> getAll();

    int size();

}
