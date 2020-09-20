package com.github.bdqfork.registry;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;
import com.github.bdqfork.core.util.StringUtils;
import com.github.bdqfork.rpc.registry.AbstractRegistry;
import com.github.bdqfork.rpc.registry.Notifier;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * zookeeper注册中心
 *
 * @author bdq
 * @since 2020/2/23
 */
public class ZookeeperRegistry extends AbstractRegistry {

    private static final Logger log = LoggerFactory.getLogger(ZookeeperRegistry.class);
    private final static String DEFAULT_ROOT = "rpc";
    private final static String PATH_SEPARATOR = "/";
    private final String root;
    private final static String ZK_SESSION_EXPIRE_KEY = "zk.session.expire";
    private final static int DEFAULT_CONNECTION_TIMEOUT_MS = 5 * 1000;
    private final static int DEFAULT_SESSION_TIMEOUT_MS = 60 * 1000;
    private final static String DEFAULT_ADDRESS = "127.0.0.1:2181";
    private final Map<String, URL> cacheNodes = new ConcurrentHashMap<>();
    private Map<String, CacheWatcher> cacheWatchers = new ConcurrentHashMap<>();
    private CuratorFramework client;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public ZookeeperRegistry(URL url) {
        super(url);
        this.root = url.getParam(ProtocolProperty.GROUP, DEFAULT_ROOT);
    }

    @Override
    public void register(URL url) {
        try {
            createPersistent(toParentPath(url));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            createEphemeral(toPath(url), url.toPath());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        cacheNodes.putIfAbsent(toPath(url), url);
    }

    @Override
    public void undoRegister(URL url) {
        String path = toPath(url);
        try {
            deletePath(path);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        cacheNodes.remove(toPath(url));
    }

    @Override
    public void subscribe(URL url, Notifier notifier) {
        String providerPath = toServicePath(url) + PATH_SEPARATOR + ProtocolProperty.PROVIDER;
        notifier.notify(lookup(url));
        PathChildrenCache cache = new PathChildrenCache(client, providerPath, false);
        try {
            cache.start();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("PathChildrenCache start failed!", e.getCause());
            }
            throw new IllegalStateException(e);
        }
        PathChildrenCacheListener cacheListener = (c, event) -> {
            PathChildrenCacheEvent.Type type = event.getType();
            if (type == PathChildrenCacheEvent.Type.CHILD_ADDED || type == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                notifier.notify(lookup(url));
            }
        };
        cache.getListenable().addListener(cacheListener);
        cacheWatchers.putIfAbsent(providerPath, new CacheWatcher(url, notifier));
    }

    @Override
    public List<URL> lookup(URL url) {
        List<URL> providers = new ArrayList<>();
        String providerPath = toServicePath(url) + PATH_SEPARATOR + ProtocolProperty.PROVIDER;
        List<String> children = null;
        try {
            if (client.checkExists().forPath(providerPath) != null) {
                children = client.getChildren().forPath(providerPath);
            }
        }catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("lookup failed! cause", e.getCause());
            }
            throw new IllegalStateException(e);
        }
        if (children == null) {
            return providers;
        }
        for (String s : children) {
            String content = doGetContent(providerPath + PATH_SEPARATOR + s);
            if (!StringUtils.isEmpty(content)){
                providers.add(URL.fromString(content));
            }
        }
        return providers;
    }

    @Override
    protected void doConnect() {
        URL url = getUrl();
        int timeout = url.getParam(ProtocolProperty.TIMEOUT, DEFAULT_CONNECTION_TIMEOUT_MS);
        int sessionExpireMs = url.getParam(ZK_SESSION_EXPIRE_KEY, DEFAULT_SESSION_TIMEOUT_MS);
        int retryTimes = url.getParam(ProtocolProperty.RETRIES, 3);
        String addresses = url.getParam(ProtocolProperty.ADDRESS, DEFAULT_ADDRESS).replace(';', ',');
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(addresses)
                .retryPolicy(new RetryNTimes(retryTimes, 1000))
                .connectionTimeoutMs(timeout)
                .sessionTimeoutMs(sessionExpireMs);
        String username = url.getParam(ProtocolProperty.USERNAME);
        String password = url.getParam(ProtocolProperty.PASSWORD);
        if (username != null && password != null) {
            String authority = username + ":" + password;
            builder = builder.authorization("digest", authority.getBytes());
        }
        client = builder.build();
        client.start();
        client.getConnectionStateListenable().addListener((client, newState) -> {
            if (newState.isConnected()) {
                available = true;
                recover();
            } else if (!destroyed.get()) {
                available = false;
                try {
                    client.blockUntilConnected(retryTimes, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    if (log.isErrorEnabled()) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        });
    }

    private void recover() {
        cacheNodes.values().forEach(this::register);
        cacheWatchers.values().forEach(watcher -> subscribe(watcher.getUrl(), watcher.getNotifier()));
    }

    @Override
    protected void doDestroy() {
        client.close();
    }


    /**
     * 创建永久节点
     * @param path 节点路径
     */
    private void createPersistent(String path) throws Exception{
        try {
            client.create().creatingParentsIfNeeded().forPath(path);
        } catch (KeeperException.NodeExistsException ignored) {
            //节点如已存在，忽略异常
        }
    }

    /**
     * 创建临时节点
     * @param path 节点路径
     * @param data 节点数据
     */
    private void createEphemeral(String path, String data) throws Exception{
        byte[] dataBytes = data.getBytes(CHARSET);
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, dataBytes);
        } catch (KeeperException.NodeExistsException e) {
            if (log.isWarnEnabled()) {
                log.warn("ZNode " + path + " already exists, we will delete it and recreate", e);
            }
            deletePath(path);
            createEphemeral(path, data);
        }
    }

    /**
     *删除目标目录
     * @param path 节点路径
     */
    private void deletePath(String path) throws Exception{
        try {
            client.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (KeeperException.NoNodeException ignored) {
            //节点已删除，忽略
        }
    }


    /**
     *获取指定path下的数据
     * @param path 节点路径
     * @return 节点数据
     */
    private String doGetContent(String path) {
        byte[] dataBytes;
        try {
            dataBytes = client.getData().forPath(path);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e.getCause());
            }
            return null;
        }
        return (dataBytes == null || dataBytes.length == 0) ? null : new String(dataBytes, CHARSET);
    }

    /**
     *将url转换为其接口代表的目录
     * @param url service url
     * @return path for service interface
     */
    private String toServicePath(URL url) {
        return PATH_SEPARATOR + url.getParam(ProtocolProperty.GROUP, root) + PATH_SEPARATOR + url.getServiceName();
    }

    /**
     *将url转化为最后节点的父目录
     * @param url service url
     * @return service parent path
     */
    private String toParentPath(URL url) {
        return toServicePath(url) + PATH_SEPARATOR + url.getProtocol();
    }

    /**
     *将url转化为完整的path
     * @param url service url
     * @return service path
     */
    private String toPath(URL url) {
        return toParentPath(url) + PATH_SEPARATOR + URL.encode(url.toPath());
    }

    private class CacheWatcher {
        private final URL url;
        private final Notifier notifier;

        CacheWatcher(URL url, Notifier notifier) {
            this.url = url;
            this.notifier = notifier;
        }

        URL getUrl() {
            return url;
        }

        Notifier getNotifier() {
            return notifier;
        }
    }
}
