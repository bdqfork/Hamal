package com.github.bdqfork.rpc.container;

import com.github.bdqfork.core.exception.ConfilictServiceException;
import com.github.bdqfork.core.extension.SPI;
import com.github.bdqfork.rpc.Invoker;

import java.util.Map;

/**
 * 服务容器，用于存储服务实例
 *
 * @author bdq
 * @since 2020/2/26
 */
@SPI("rpc")
public interface ServiceContainer {
    /**
     * 存储服务实例
     *
     * @param serviceName 服务名称
     * @param invoker     服务的Invoker实例
     * @throws ConfilictServiceException 注册已存在的服务时抛出
     */
    void regsiter(String serviceName, Invoker<?> invoker) throws ConfilictServiceException;

    /**
     * 移除服务实例
     *
     * @param serviceName 服务名称
     */
    void remove(String serviceName);

    /**
     * 获取服务实例
     * @param serviceName 服务名称
     * @return 服务实例
     */
    Invoker<?> get(String serviceName);

    /**
     * 获取所有服务实例
     * @return 所有服务实例
     */
    Map<String, Invoker<?>> getAll();

    /**
     * 获取当前服务实例数量
     * @return 当前服务实例数量
     */
    int size();

}
