package cn.hamal.rpc.protocol;

import cn.hamal.core.URL;
import cn.hamal.core.extension.SPI;
import cn.hamal.rpc.Invoker;
import cn.hamal.rpc.registry.exporter.Exporter;

/**
 * @author bdq
 * @since 2020/2/26
 */
@SPI("rpc")
public interface Protocol {

    /**
     * 开启服务
     *
     * @param url
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
     * @param serviceInterface
     * @param url
     * @return
     */
    <T> Invoker<T> refer(Class<T> serviceInterface, URL url);

    /**
     * 关闭协议
     */
    void destroy();

}
