package com.github.bdqfork.context.config;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;
import com.github.bdqfork.core.util.StringUtils;

/**
 * 服务提供者配置类
 *
 * @author bdq
 * @since 2020/2/26
 */
public class ServiceConfig<T> {
    /**
     * 服务分组
     */
    private String group = ProtocolProperty.DEFAULT_GROUP;
    /**
     * 超时时间
     */
    private long timeout = ProtocolProperty.DEFAULT_TIMEOUT;
    /**
     * 服务类型
     */
    private Class<T> serviceInterface;
    /**
     * 版本
     */
    private String version;
    /**
     * 地址
     */
    private String host;
    /**
     * 端口
     */
    private Integer port;

    public ServiceConfig(Class<T> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public URL toURL() {
        URL url = new URL(ProtocolProperty.PROVIDER, host, port, serviceInterface.getCanonicalName());
        url.addParam(ProtocolProperty.GROUP, group);
        if (!StringUtils.isEmpty(version)) {
            url.addParam(ProtocolProperty.VERSION, version);
        }
        url.addParam(ProtocolProperty.TIMEOUT, timeout);
        return url;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Class<T> getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class<T> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
}
