package com.github.bdqfork.rpc.config;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;
import com.github.bdqfork.core.util.StringUtils;
import com.github.bdqfork.rpc.annotation.Service;

/**
 * 服务提供者配置类
 *
 * @author bdq
 * @since 2020/2/26
 */
public class ServiceConfig<T> {
    private String group = ProtocolProperty.DEFAULT_GROUP;
    private long timeout = ProtocolProperty.DEFAULT_TIMEOUT;
    private Class<T> serviceInterface;
    private String version;
    private String host;
    private Integer port;

    @SuppressWarnings("unchecked")
    public ServiceConfig(Service service) {
        this.group = service.group();
        this.timeout = service.timeout();
        this.version = service.version();
        this.serviceInterface = (Class<T>) service.serviceInterface();
    }

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
