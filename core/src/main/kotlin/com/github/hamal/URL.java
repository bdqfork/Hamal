package com.github.hamal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * rpc://10.20.153.10:1234/barService?param=value
 *
 * @author bdq
 * @since 2020/2/23
 */
public class URL implements Serializable {
    /**
     * 服务协议,consumer或者provider
     */
    private String protocol;
    /**
     * 服务地址
     */
    private String host;
    /**
     * 服务端口
     */
    private Integer port;
    /**
     * 服务名
     */
    private String path;
    /**
     * 参数
     */
    private Map<String, Object> params = new HashMap<>();

    public URL(String protocol, String host, Integer port, String path) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public void addParams(Map<String, Object> params) {
        this.params.putAll(params);
    }

    @SuppressWarnings("unchecked")
    public <T> T getParam(String key, T defaultValue) {
        if (params.containsKey(key)) {
            return (T) params.get(key);
        }
        return defaultValue;
    }

    public void clearParams() {
        params.clear();
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }
}
