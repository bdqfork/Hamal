package com.github.hamal;

import com.github.hamal.registry.Notifier;

import java.util.List;

/**
 * zookeeper注册中心
 *
 * @author bdq
 * @since 2020/2/23
 */
public class ZookeeperRegistry extends AbstractRegistry {

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
    protected void doDestroy() {

    }
}
