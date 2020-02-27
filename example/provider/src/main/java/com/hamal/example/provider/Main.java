package com.hamal.example.provider;

import cn.hamal.rpc.annotation.Application;
import cn.haml.context.ContextManager;

import java.util.concurrent.CountDownLatch;

/**
 * @author bdq
 * @since 2020/2/26
 */
@Application(direct = true)
public class Main {
    public static void main(String[] args) throws Exception {
        ContextManager contextManager = ContextManager.build(Main.class);
        contextManager.registerService(new UserServiceImpl());
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }
}
