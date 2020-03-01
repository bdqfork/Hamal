package com.github.bdqfork.example.consumer;

import com.github.bdqfork.context.ContextManager;
import com.github.bdqfork.example.api.User;
import com.github.bdqfork.example.api.UserService;
import com.github.bdqfork.rpc.annotation.Application;
import com.github.bdqfork.rpc.config.ReferenceConfig;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author bdq
 * @since 2020/3/1
 */
@Application(direct = true)
public class HeartBeatTest {
    public static void main(String[] args) throws Exception {
        ContextManager contextManager = ContextManager.build(Main.class);
        ReferenceConfig<?> referenceConfig = new ReferenceConfig<>(UserService.class);
        referenceConfig.setConnections(2);

        UserService userService = (UserService) contextManager.getProxy(referenceConfig);
        userService.getUser(1L);
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }
}
