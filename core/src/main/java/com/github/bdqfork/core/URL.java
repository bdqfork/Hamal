package com.github.bdqfork.core;

import com.github.bdqfork.core.util.StringUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * rpc://10.20.153.10:1234/barService?param=value
 *
 * @author bdq
 * @since 2020/2/23
 */
public class URL implements Serializable {
    /**
     * 服务协议
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
    private String serviceName;
    /**
     * 参数
     */
    private Map<String, Object> params = new HashMap<>();

    public URL(String protocol, String host, Integer port, String serviceName) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.serviceName = serviceName;
    }

    public void addParam(String key, Object value) {
        this.params.put(key, value);
    }

    public void addParams(Map<String, Object> params) {
        this.params.putAll(params);
    }

    @SuppressWarnings("unchecked")
    public <T> T getParam(String key) {
        return (T) params.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getParam(String key, T defaultValue) {
        Object value = params.get(key);
        if ((defaultValue instanceof Long || defaultValue instanceof Integer) && value instanceof String) {
            return (T) Long.valueOf((String) value);
        }
        return (T) params.getOrDefault(key, defaultValue);
    }

    public Map<String, Object> getParams() {
        return Collections.unmodifiableMap(params);
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

    public String getServiceName() {
        return serviceName;
    }

    /**
     * 将URL序列化为字符串
     *
     * @return URL字符串
     */
    public String toPath() {
        StringBuilder builder = new StringBuilder();
        builder.append(protocol).append("://").append(host).append(":").append(port).append("/").append(serviceName);
        if (params.size() > 0) {
            String[] keys = params.keySet().toArray(new String[0]);
            builder.append("?").append(keys[0]).append("=").append(params.get(keys[0]));
            for (int i = 1; i < keys.length; i++) {
                builder.append("&").append(keys[i]).append("=").append(params.get(keys[i]) == null ? "" : params.get(keys[i]));
            }
        }
        return builder.toString();
    }

    /**
     * 从URL字符串反序列化为URL对象
     *
     * @param urlString URL字符串
     * @return URL实例
     */
    public static URL fromString(String urlString) {
        String[] strs = urlString.split("://");

        String protocol = strs[0];
        strs = strs[1].split("/");
        String address = strs[0];

        String[] hostAndPort = address.split(":");

        String[] serviceStr = strs[1].split("\\?");
        String serviceName = serviceStr[0];

        URL url = new URL(protocol, hostAndPort[0], Integer.valueOf(hostAndPort[1]), serviceName);

        if (serviceStr.length < 2) {
            return url;
        }
        String paramStr = serviceStr[1];
        String[] params = paramStr.split("&");

        for (String param : params) {
            String[] pairs = param.split("=");
            url.addParam(pairs[0], pairs[1]);
        }
        return url;
    }

    public static String encode(String value) {
        if (StringUtils.isEmpty(value)) {
            return "";
        }
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        String url = ((URL)o).toPath();
        return url.equals(toPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(protocol, host, port, serviceName, params);
    }
}