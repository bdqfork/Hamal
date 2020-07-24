package com.github.bdqfork.context.config;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;
import com.github.bdqfork.core.util.NetUtils;
import com.github.bdqfork.core.util.StringUtils;

/**
 * 消费者配置类
 *
 * @author bdq
 * @since 2020/2/26
 */
public class ReferenceConfig<T> {
    /**
     * 服务的分组
     */
    private String group = ProtocolProperty.DEFAULT_GROUP;
    /**
     * 超时时间
     */
    private long timeout = ProtocolProperty.DEFAULT_TIMEOUT;
    /**
     * 连接数
     */
    private int connections = 1;
    /**
     * 服务类型
     */
    private Class<T> serviceInterface;
    /**
     * 服务版本
     */
    private String version;

    public ReferenceConfig(Class<T> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public URL toURL() {
        URL url = new URL(ProtocolProperty.CONSUMER, NetUtils.getLocalHost(), 0, serviceInterface.getCanonicalName());
        url.addParam(ProtocolProperty.GROUP, group);
        url.addParam(ProtocolProperty.TIMEOUT, timeout);
        url.addParam(ProtocolProperty.CONNECTIONS, connections);
        if (!StringUtils.isEmpty(version)) {
            url.addParam(ProtocolProperty.VERSION, version);
        }
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

    public int getConnections() {
        return connections;
    }

    public void setConnections(int connections) {
        this.connections = connections;
    }
}
