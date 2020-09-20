package com.github.bdqfork.example.consumer;

import com.github.bdqfork.context.ContextManager;
import com.github.bdqfork.example.api.User;
import com.github.bdqfork.example.api.UserService;
import com.github.bdqfork.rpc.config.ApplicationConfig;
import com.github.bdqfork.rpc.config.ReferenceConfig;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainWithAPI {

    public static void main(String[] args) {
        ApplicationConfig applicationConfig = new ApplicationConfig("127.0.0.1", 8080, "rpc", "netty");
        applicationConfig.setContainer("rpc");
        applicationConfig.setDirect(true);
        applicationConfig.setLoadbalancer("random");
        applicationConfig.setSerilizer("hessian");
        ContextManager contextManager = new ContextManager(applicationConfig);
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
