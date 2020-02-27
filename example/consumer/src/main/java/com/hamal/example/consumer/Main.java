package com.hamal.example.consumer;

import cn.hamal.rpc.annotation.Application;
import cn.hamal.rpc.config.ReferenceConfig;
import cn.haml.context.ContextManager;
import com.hamal.example.api.User;
import com.hamal.example.api.UserService;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author bdq
 * @since 2020/2/26
 */
@Application(direct = true, startUp = false)
public class Main {
    public static void main(String[] args) throws Exception {
        ContextManager contextManager = ContextManager.build(Main.class);
        ReferenceConfig<?> referenceConfig = new ReferenceConfig<>(UserService.class);
        referenceConfig.setConnections(2);

        UserService userService = (UserService) contextManager.getProxy(referenceConfig);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(40, 50, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(512),
                new ThreadPoolExecutor.DiscardPolicy());
        while (true) {
            executor.execute(() -> {
                User user = userService.getUser(1L);
                System.out.println(user);
            });
            Thread.sleep(2000);
        }
    }
}
