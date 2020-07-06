package com.github.bdqfork.example.provider;

import com.github.bdqfork.context.ContextManager;
import com.github.bdqfork.example.api.UserService;
import com.github.bdqfork.rpc.config.ApplicationConfig;
import com.github.bdqfork.rpc.config.ServiceConfig;

import java.util.concurrent.CountDownLatch;

public class MainWithAPI {

    public static void main(String[] args) throws Exception {
        ApplicationConfig applicationConfig = new ApplicationConfig("127.0.0.1", 8080, "rpc", "netty");
        applicationConfig.setContainer("rpc");
        applicationConfig.setDirect(true);
        applicationConfig.setLoadbalancer("random");
        applicationConfig.setSerilizer("hessian");
        ContextManager contextManager = new ContextManager(applicationConfig);
        ServiceConfig<UserService> serviceConfig = new ServiceConfig<>(UserService.class);
        contextManager.registerService(new UserServiceImpl(), serviceConfig);
        contextManager.open();
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }
}
