package cn.hamal.registry;

import cn.hamal.core.URL;
import cn.hamal.core.registry.Notifier;

import java.util.List;

/**
 * zookeeper注册中心
 *
 * @author bdq
 * @since 2020/2/23
 */
public class ZookeeperRegistry extends AbstractRegistry {

    public ZookeeperRegistry(URL url) {
        super(url);
    }

    @Override
    public void register(URL url) {

    }

    @Override
    public void undoRegister(URL url) {

    }

    @Override
    public void subscribe(URL url, Notifier notifier) {

    }

    @Override
    public List<URL> lookup(URL url) {
        return null;
    }

    @Override
    protected void doConnect() {

    }

    @Override
    protected void doDestroy() {

    }

}
