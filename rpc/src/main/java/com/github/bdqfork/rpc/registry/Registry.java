package com.github.bdqfork.rpc.registry;

import com.github.bdqfork.core.Node;
import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.extension.SPI;

import java.util.List;

/**
 * 注册中心接口
 *
 * @author bdq
 * @since 2020/2/23
 */
@SPI("zookeeper")
public interface Registry extends Node {

    /**
     * 注册服务
     *
     * @param url 需要注册的服务信息
     */
    void register(URL url);

    /**
     * 注销服务
     *
     * @param url 需要撤销的服务信息
     */
    void undoRegister(URL url);

    /**
     * 订阅服务
     *
     * @param url      需要订阅的服务信息
     * @param notifier 服务上下线通知
     */
    void subscribe(URL url, Notifier notifier);

    /**
     * 返回服务信息
     *
     * @param url 查询的服务信息
     * @return 可用的服务信息列表
     */
    List<URL> lookup(URL url);

}