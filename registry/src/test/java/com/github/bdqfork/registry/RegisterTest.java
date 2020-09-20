package com.github.bdqfork.registry;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.rpc.registry.Registry;

import org.apache.curator.test.TestingServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RegisterTest {

    private static Registry registry;
    private static TestingServer server;
    private final static String serviceUrl = "provider://127.0.0.1:8081/com.github.bdqfork.registry.UserService?timeout=60000&group=rpc";
    private final static String referenceUrl = "consumer://172.27.253.215:0/com.github.bdqfork.registry.UserService?timeout=60000&connections=1&group=rpc";

    @BeforeAll
    public static void setUp() throws Exception {
        server = new TestingServer(2181, new File("~/zk-book-data"));
        URL url = URL.fromString("registry://127.0.0.1:0/zookeeper?address=127.0.0.1:2181");
        registry = new DefaultRegistryFactory().getRegistry(url);
    }

    @Test
    public void testRegister() {
        registry.register(URL.fromString(serviceUrl));
    }

    @Test
    public void testLookup() {
        registry.register(URL.fromString(serviceUrl));
        List<URL> res = registry.lookup(URL.fromString(referenceUrl));
        assert (res.get(0).equals(URL.fromString(serviceUrl)));
    }

    @Test
    public void testSubscribe() throws InterruptedException {
        registry.register(URL.fromString(serviceUrl));
        CountDownLatch latch = new CountDownLatch(4);
        registry.subscribe(URL.fromString(referenceUrl), urls -> {
            if (urls.size() == 0) {
                latch.countDown();
            }
            urls.forEach(e -> {
                if (e.equals(URL.fromString(serviceUrl))) {
                    latch.countDown();
                }
            });
        });
        Thread.sleep(100);
        registry.undoRegister(URL.fromString(serviceUrl));
        Thread.sleep(100);
        registry.register(URL.fromString(serviceUrl));
        assert (latch.await(3, TimeUnit.SECONDS));
    }

    @Test
    public void testReConnect() throws Exception {
        registry.register(URL.fromString(serviceUrl));
        CountDownLatch latch = new CountDownLatch(4);
        registry.subscribe(URL.fromString(referenceUrl), urls -> {
            urls.forEach(e -> {
                if (e.equals(URL.fromString(serviceUrl))) {
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
