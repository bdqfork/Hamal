package cn.hamal.rpc.config;

import cn.hamal.core.URL;
import cn.hamal.rpc.annotation.Application;

/**
 * 应用配置类
 *
 * @author bdq
 * @since 2020/2/26
 */
public class ApplicationConfig {
    private String host;
    private Integer port;
    private String protocol;
    private String server;
    private String serilizer;
    private boolean direct;
    private String loadbalancer;
    private String container;

    public ApplicationConfig(Application application) {
        this.host = application.host();
        this.port = application.port();
        this.protocol = application.protocol();
        this.server = application.server();
        this.serilizer = application.serilizer();
        this.direct = application.direct();
        this.loadbalancer = application.loadbalancer();
        this.container = application.container();
    }

    public ApplicationConfig(String host, Integer port, String protocol, String server) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.server = server;
    }

    public URL toURL() {
        return new URL(protocol, host, port, server);
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
