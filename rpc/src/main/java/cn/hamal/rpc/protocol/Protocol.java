package cn.hamal.rpc.protocol;

import cn.hamal.core.URL;
import cn.hamal.core.extension.SPI;
import cn.hamal.rpc.Invoker;
import cn.hamal.rpc.registry.exporter.Exporter;

/**
 * 通信协议
 *
 * @author bdq
 * @since 2020/2/26
 */
@SPI("rpc")
public interface Protocol {

    /**
     * 开启服务
     *
     * @param url 服务信息
     */
    void open(URL url) throws Exception;

    /**
     * 获取服务发布者
     *
     * @param url exporter信息
     */
    Exporter export(URL url);

    /**
     * 获取消费者Invoker
     *
     * @param serviceInterface 服务接口
     * @param url              消费者信息
     * @return 消费者Invoker实例
     */
    <T> Invoker<T> refer(Class<T> serviceInterface, URL url);

    /**
     * 关闭服务
     */
    void destroy();

}
