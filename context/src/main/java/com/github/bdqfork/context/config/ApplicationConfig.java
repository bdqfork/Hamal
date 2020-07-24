package com.github.bdqfork.context.config;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;

/**
 * 应用配置类
 *
 * @author bdq
 * @since 2020/2/26
 */
public class ApplicationConfig {
    /**
     * 地址
     */
    private String host;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 协议名称
     */
    private String protocol;
    /**
     * 服务器类型
     */
    private String server;
    /**
     * 序列化方式
     */
    private String serilizer;
    /**
     * 是否开启端到端直连
     */
    private boolean direct;
    /**
     * 负载均衡策略
     */
    private String loadbalancer;
    /**
     * service容器类型
     */
    private String container;

    public ApplicationConfig(String host, Integer port, String protocol, String server) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.server = server;
    }

    public URL toURL() {
        URL url = new URL(protocol, host, port, server);
        url.addParam(ProtocolProperty.SERIALIZER, serilizer);
        url.addParam(ProtocolProperty.DIRECT, direct);
        url.addParam(ProtocolProperty.LOAD_BALANCER, loadbalancer);
        url.addParam(ProtocolProperty.CONTATINER, container);
        return url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSerilizer() {
        return serilizer;
    }

    public void setSerilizer(String serilizer) {
        this.serilizer = serilizer;
    }

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public String getLoadbalancer() {
        return loadbalancer;
    }

    public void setLoadbalancer(String loadbalancer) {
        this.loadbalancer = loadbalancer;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }
}
