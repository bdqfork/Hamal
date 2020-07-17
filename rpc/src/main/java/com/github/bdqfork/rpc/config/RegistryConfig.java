package com.github.bdqfork.rpc.config;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;
import com.github.bdqfork.core.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册中心配置类
 * @author bdq
 * @since 2020/2/26
 */
public class RegistryConfig {


    String host;

    Integer port;

    String type;

    Map<String, Integer> addresses = new HashMap<>();

    String username;

    String password;

    Integer timeout;

    Integer retryTime;

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

    public Map<String, Integer> getAddresses() {
        return addresses;
    }

    public void setAddresses(Map<String, Integer> addresses) {
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

    public RegistryConfig(String host, Integer port, String type) {
        this.host = host;
        this.port = port;
        this.type = type;
    }

    public URL toURL(){
        URL url = new URL(ProtocolProperty.REGISTRY, host, port, type);
        if (!StringUtils.isEmpty(username)) {
            url.addParam(ProtocolProperty.USERNAME, username);
        }
        if (!StringUtils.isEmpty(password)) {
            url.addParam(ProtocolProperty.PASSWORD, password);
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
