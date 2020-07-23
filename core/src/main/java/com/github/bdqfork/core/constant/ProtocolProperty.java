package com.github.bdqfork.core.constant;

/**
 * @author bdq
 * @since 2020/2/25
 */
public class ProtocolProperty {
    public static final String PROTOCOL = "protocol";
    public static final String GROUP = "group";
    public static final String VERSION = "version";
    public static final String TIMEOUT = "timeout";
    public static final String LOAD_BALANCER = "loadbalancer";
    public static final String REGISTRY = "registry";
    public static final String CONSUMER = "consumer";
    public static final String PROVIDER = "provider";
    public static final String CONNECTIONS = "connections";
    public static final String WEIGHT = "weight";
    public static final String EXPORTER = "exporter";
    public static final String DIRECT = "direct";
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String SERVER = "server";
    public static final String SERIALIZER = "serializer";
    public static final String RETRIES = "reties";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ADDRESS = "address";

    public static final long DEFAULT_TIMEOUT = 60 * 1000;
    public static final String DEFAULT_GROUP = "rpc";
    public static final String DEFAULT_SERIALIZER = "hessian";
    public static final String CONTATINER = "container";
    public static final String DEFAULT_CONTAINER = "rpc";
}
