package com.github.bdqfork.registry;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.rpc.config.ReferenceConfig;
import com.github.bdqfork.rpc.config.ServiceConfig;
import com.github.bdqfork.rpc.registry.Registry;

import org.apache.curator.test.TestingServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RegisterTest {

    static Registry registry;
    static TestingServer server;

    @BeforeAll
    public static void setUp() throws Exception {
        server = new TestingServer(2181, new File("~/zk-book-data"));
        URL url = URL.fromString("registry://127.0.0.1:0/zookeeper?address=127.0.0.1:2181");
        registry = new DefaultRegistryFactory().getRegistry(url);
    }

    @Test
    public void testRegister() {
        ServiceConfig<UserService> serviceConfig = new ServiceConfig<>(UserService.class);
        serviceConfig.setHost("127.0.0.1");
        serviceConfig.setPort(8081);
        registry.register(serviceConfig.toURL());
    }

    @Test
    public void testLookup() {
        ServiceConfig<UserService> serviceConfig = new ServiceConfig<>(UserService.class);
        serviceConfig.setHost("127.0.0.1");
        serviceConfig.setPort(8081);
        registry.register(serviceConfig.toURL());
        ReferenceConfig<?> referenceConfig = new ReferenceConfig<>(UserService.class);
        List<URL> res = registry.lookup(referenceConfig.toURL());
        assert (res.get(0).equals(serviceConfig.toURL()));
    }

    @Test
    public void testSubscribe() throws InterruptedException {
        ServiceConfig<UserService> serviceConfig = new ServiceConfig<>(UserService.class);
        serviceConfig.setHost("127.0.0.1");
        serviceConfig.setPort(8081);
        registry.register(serviceConfig.toURL());
        ReferenceConfig<?> referenceConfig = new ReferenceConfig<>(UserService.class);
        CountDownLatch latch = new CountDownLatch(4);
        registry.subscribe(referenceConfig.toURL(), urls -> {
            if (urls.size() == 0) {
                latch.countDown();
            }
            urls.forEach(e -> {
                if (e.equals(serviceConfig.toURL())) {
                    latch.countDown();
                }
            });
        });
        Thread.sleep(100);
        registry.undoRegister(serviceConfig.toURL());
        Thread.sleep(100);
        registry.register(serviceConfig.toURL());
        assert (latch.await(3, TimeUnit.SECONDS));
    }

    @Test
    public void testReConnect() throws Exception {
        ServiceConfig<UserService> serviceConfig = new ServiceConfig<>(UserService.class);
        serviceConfig.setHost("127.0.0.1");
        serviceConfig.setPort(8081);
        registry.register(serviceConfig.toURL());
        ReferenceConfig<?> referenceConfig = new ReferenceConfig<>(UserService.class);
        CountDownLatch latch = new CountDownLatch(4);
        registry.subscribe(referenceConfig.toURL(), urls -> {
            urls.forEach(e -> {
                if (e.equals(serviceConfig.toURL())) {
                    latch.countDown();
                }
            });
        });
        server.restart();
        assert (latch.await(3, TimeUnit.SECONDS));
    }

    @AfterAll
    public static void destroy() throws IOException {
        registry.destroy();
        server.close();
    }

}
