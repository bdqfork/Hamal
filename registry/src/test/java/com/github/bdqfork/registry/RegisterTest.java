package com.github.bdqfork.registry;

import com.github.bdqfork.core.URL;
import com.github.bdqfork.rpc.config.ReferenceConfig;
import com.github.bdqfork.rpc.config.ServiceConfig;
import com.github.bdqfork.rpc.registry.Registry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RegisterTest {

    static Registry registry;

    @BeforeAll
    public static void setUp() {
        URL url = URL.fromString("registry://127.0.0.1:2181/zookeeper");
        registry = new DefaultRegistryFactory().getRegistry(url);
    }

    @Test
    public void testRegister() {
        ServiceConfig<UserService> serviceConfig = new ServiceConfig<>(UserService.class);
        registry.register(serviceConfig.toURL());
        registry.undoRegister(serviceConfig.toURL());
    }

    @Test
    public void testLookup() {
        ServiceConfig<UserService> serviceConfig = new ServiceConfig<>(UserService.class);
        serviceConfig.setHost("127.0.0.1");
        serviceConfig.setPort(8081);
        registry.register(serviceConfig.toURL());
        ReferenceConfig<?> referenceConfig = new ReferenceConfig<>(UserService.class);
        registry.lookup(referenceConfig.toURL());
    }

    @Test
    public void testSubscribe() {
        ServiceConfig<UserService> serviceConfig = new ServiceConfig<>(UserService.class);
        serviceConfig.setHost("127.0.0.1");
        serviceConfig.setPort(8081);
        registry.register(serviceConfig.toURL());
        ReferenceConfig<?> referenceConfig = new ReferenceConfig<>(UserService.class);
        registry.subscribe(referenceConfig.toURL(), urls -> urls.forEach(e->{
            System.out.println(e.toPath());
        }));
    }

    @AfterAll
    public static void destroy() {
        registry.destroy();
    }

}
