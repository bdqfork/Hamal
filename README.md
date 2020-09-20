一个基于netty的轻量级rpc框架，使用方式可参考example。

## Features:
- 基于netty实现rpc协议
- 支持端到端的rpc调用
- 支持使用SPI进行功能扩展

## GetStart
引入依赖
```groovy
implementation "com.github.bdqfork:hamal-context:0.1.1"
```
定义接口，例如UserService
```java
public interface UserService {
    User getUser(Long id);
}
```
在服务端实现接口
```java
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("testRpc");
        user.setPassword("testpass");
        return user;
    }
}
```
初始化上下文，启动服务端
```java
public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationConfig applicationConfig = new ApplicationConfig("127.0.0.1", 8081, "rpc", "netty");
        applicationConfig.setContainer("rpc");
        applicationConfig.setDirect(true);
        applicationConfig.setLoadbalancer("random");
        applicationConfig.setSerilizer("hessian");
        Bootstrap bootstrap = new Bootstrap(applicationConfig);
        ServiceConfig<UserService> serviceConfig = new ServiceConfig<>(UserService.class);
        bootstrap.registerService(new UserServiceImpl(), serviceConfig);
        bootstrap.open();
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }
}
```

客户端同样初始化上下文，但不需要open服务器
```java
public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationConfig applicationConfig = new ApplicationConfig("127.0.0.1", 8081, "rpc", "netty");
        applicationConfig.setContainer("rpc");
        applicationConfig.setDirect(true);
        applicationConfig.setLoadbalancer("random");
        applicationConfig.setSerilizer("hessian");
        Bootstrap bootstrap = new Bootstrap(applicationConfig);
        ReferenceConfig<?> referenceConfig = new ReferenceConfig<>(UserService.class);
        referenceConfig.setConnections(2);

        UserService userService = (UserService) bootstrap.getProxy(referenceConfig);
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
```

## 注册中心
支持使用zookeeper作为注册中心
```java
RegistryConfig registryConfig = new RegistryConfig("zookeeper", "127.0.0.1:8080");
Bootstrap bootstrap = new Bootstrap(applicationConfig, registryConfig);
```

## 序列化
支持hessian和json两种序列号方式

```java
applicationConfig.setSerilizer("hessian");
```
or
```java
applicationConfig.setSerilizer("json");
```

## 协议
默认使用基于tcp的自定义rpc协议，支持http协议
```java
ApplicationConfig applicationConfig = new ApplicationConfig("127.0.0.1", 8081, "http", "netty");
```


## todolist:
- 实现服务分组功能
- 实现超时重试策略
- 实现快速失败策略
- 实现常见的负载均衡算法
- 实现Filter
- 支持异步调用
- 添加log信息