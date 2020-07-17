package com.github.bdqfork.registry;


import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;
import com.github.bdqfork.core.exception.RpcException;
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
import java.util.concurrent.TimeUnit;

/**
 * zookeeper注册中心
 *
 * @author bdq
 * @since 2020/2/23
 */
public class ZookeeperRegistry extends AbstractRegistry {

    protected static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);
    private final static String DEFAULT_ROOT = "hamal";
    private final static String PATH_SEPARATOR = "/";
    private final String root;
    private static final String ZK_SESSION_EXPIRE_KEY = "zk.session.expire";
    private static int DEFAULT_CONNECTION_TIMEOUT_MS = 5 * 1000;
    private static int DEFAULT_SESSION_TIMEOUT_MS = 60 * 1000;
    private CuratorFramework client;
    static final Charset CHARSET = StandardCharsets.UTF_8;

    public ZookeeperRegistry(URL url) {
        super(url);
        this.root = url.getParam(ProtocolProperty.GROUP, DEFAULT_ROOT);
    }

    @Override
    public void register(URL url) {
        try {
            createParentPath(toParentPath(url));
            createEphemeral(toPath(url), url.toPath());
        } catch (Throwable e) {
            new RpcException("Failed to register " + url + " to zookeeper " + getUrl() + ", cause: " + e.getMessage(), e).printStackTrace();
        }
    }

    @Override
    public void undoRegister(URL url) {
        try {
            deletePath(toPath(url));
        } catch (Throwable e) {
            new RpcException("Failed to register " + url + " to zookeeper " + getUrl() + ", cause: " + e.getMessage(), e).printStackTrace();
        }
    }

    @Override
    public void subscribe(URL url, Notifier notifier) {
        String providerPath = toServicePath(url) + PATH_SEPARATOR + ProtocolProperty.PROVIDER;
        notifier.notify(lookup(url));
        PathChildrenCache cache = new PathChildrenCache(client, providerPath, true);
        try {
            cache.start();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        PathChildrenCacheListener cacheListener = (c, event) -> {
            PathChildrenCacheEvent.Type type = event.getType();
            if (type == PathChildrenCacheEvent.Type.CHILD_ADDED || type == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                notifier.notify(lookup(url));
            }
        };

        cache.getListenable().addListener(cacheListener);
    }

    @Override
    public List<URL> lookup(URL url) {
        Set<URL> providers = new HashSet<>();
        String providerPath = toServicePath(url) + PATH_SEPARATOR + ProtocolProperty.PROVIDER;
        List<String> childrens = null;
        try {
            childrens = client.getChildren().forPath(providerPath);
        } catch (KeeperException.NoNodeException ignored) {
        }catch (Exception e) {
            e.printStackTrace();
        }
        if (childrens == null) {
            return null;
        }
        for (String s : childrens) {
            providers.add(URL.fromString(Objects.requireNonNull(doGetContent(providerPath + PATH_SEPARATOR + s))));
        }
        return new ArrayList<>(providers);
    }

    @Override
    protected void doConnect() {
        URL url = getUrl();
        try {
            int timeout = url.getParam(ProtocolProperty.TIMEOUT, DEFAULT_CONNECTION_TIMEOUT_MS);
            int sessionExpireMs = url.getParam(ZK_SESSION_EXPIRE_KEY, DEFAULT_SESSION_TIMEOUT_MS);
            int retryTimes = url.getParam(ProtocolProperty.RETRIES, 1);
            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                    .connectString(url.getHost()+":"+url.getPort())
                    .retryPolicy(new RetryNTimes(retryTimes, 1000))
                    .connectionTimeoutMs(timeout)
                    .sessionTimeoutMs(sessionExpireMs);
            String username = url.getParam(ProtocolProperty.USERNAME, "");
            String password = url.getParam(ProtocolProperty.PASSWORD, "");
            if ((!StringUtils.isEmpty(username))&&(!StringUtils.isEmpty(password))) {
                String authority = username + ":" + password;
                builder = builder.authorization("digest", authority.getBytes());
            }
            client = builder.build();
            client.start();
            boolean connected = client.blockUntilConnected(timeout, TimeUnit.MILLISECONDS);
            if (!connected) {
                throw new IllegalStateException("zookeeper not connected");
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    protected void doDestroy() {
        client.close();
    }

    private void createParentPath(String path) {
        int i = path.lastIndexOf('/');
        if (i > 0) {
            createParentPath(path.substring(0, i));
        }
        createPersistent(path);
    }

    private void createPersistent(String path) {
        try {
            client.create().forPath(path);
        } catch (KeeperException.NodeExistsException e) {
            // ignore it
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void createEphemeral(String path, String data) {
        byte[] dataBytes = data.getBytes(CHARSET);
        try {
            client.create().withMode(CreateMode.EPHEMERAL).forPath(path, dataBytes);
        } catch (KeeperException.NodeExistsException e) {
            logger.warn("ZNode " + path + " already exists, we just delete is and recreate", e);
            deletePath(path);
            createEphemeral(path, data);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void deletePath(String path) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (KeeperException.NoNodeException ignored) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    private String doGetContent(String path) {
        try {
            byte[] dataBytes = client.getData().forPath(path);
            return (dataBytes == null || dataBytes.length == 0) ? null : new String(dataBytes, CHARSET);
        } catch (KeeperException.NoNodeException e) {
            // ignore NoNode Exception.
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return null;
    }

    private String toServicePath(URL url) {
        return PATH_SEPARATOR + url.getParam(ProtocolProperty.GROUP, root) + PATH_SEPARATOR + url.getServiceName();
    }

    private String toParentPath(URL url) {
        return toServicePath(url) + PATH_SEPARATOR + url.getProtocol();
    }

    private String toPath(URL url) {
        return toParentPath(url) + PATH_SEPARATOR + URL.encode(url.toPath());
    }

}
