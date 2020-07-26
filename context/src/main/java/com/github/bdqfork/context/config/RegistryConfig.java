package com.github.bdqfork.context.config;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;
import com.github.bdqfork.core.util.StringUtils;


/**
 * 注册中心配置类
 * @author bdq
 * @since 2020/2/26
 */
public class RegistryConfig {


    private String host;

    private Integer port;

    /**
     * 注册中心类型，支持zookeeper
     */
    private String type;

    /**
     * 注册中心地址，格式：地址+":"+端口
     * 如果有多个地址则用";"隔开
     */
    private String addresses;

    /**
     * 注册中心用户名
     */
    private String username;

    /**
     * 注册中心密码
     */
    private String password;

    /**
     * 超时时间
     */
    private Integer timeout;

    /**
     * 连接重试次数
     */
    private Integer retryTime;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(Integer retryTime) {
        this.retryTime = retryTime;
    }

    public RegistryConfig(String type, String addressses) {
        this.host = "127.0.0.1";
        this.port = 0;
        this.type = type;
        this.addresses = addressses;
    }

    public URL toURL(){
        URL url = new URL(ProtocolProperty.REGISTRY, host, port, type);
        if (!StringUtils.isEmpty(username)) {
            url.addParam(ProtocolProperty.USERNAME, username);
        }
        if (!StringUtils.isEmpty(password)) {
            url.addParam(ProtocolProperty.PASSWORD, password);
        }
        if (!StringUtils.isEmpty(addresses)) {
            url.addParam(ProtocolProperty.ADDRESS, addresses);
        }
        if (timeout != null) {
            url.addParam(ProtocolProperty.TIMEOUT, timeout);
        }
        if (retryTime != null) {
            url.addParam(ProtocolProperty.RETRIES, retryTime);
        }
        return url;
    }
}
