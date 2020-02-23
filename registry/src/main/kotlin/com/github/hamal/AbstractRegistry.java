package com.github.hamal;


import com.github.hamal.registry.Registry;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author bdq
 * @since 2020/2/23
 */
public abstract class AbstractRegistry implements Registry {
    protected volatile boolean available;
    protected AtomicBoolean destroyed = new AtomicBoolean(false);

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void destroy() {
        if (destroyed.compareAndSet(false, true)) {
            available = false;
            doDestroy();
        }
    }

    protected abstract void doDestroy();

}
