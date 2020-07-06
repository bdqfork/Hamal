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
@Service(serviceInterface = UserService.class)
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
初始化上下文，启动服务端，默认监听8080端口
```java
@Application(direct = true)
public class Main {
    public static void main(String[] args) throws Exception {
        ContextManager contextManager = ContextManager.build(Main.class);
        contextManager.registerService(new UserServiceImpl());
        contextManager.open();
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }
}
```
也可以不依赖注解启动
```java
public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationConfig applicationConfig = new ApplicationConfig("127.0.0.1", 8080, "rpc", "netty");
        applicationConfig.setContainer("rpc");
        applicationConfig.setDirect(true);
        applicationConfig.setLoadbalancer("random");
        applicationConfig.setSerilizer("hessian");
        ContextManager contextManager = new ContextManager(applicationConfig);
        contextManager.registerService(new UserServiceImpl());
        contextManager.open();
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }
}
```
如果实现的接口上未标注`@Service`
则需要在注册服务时如下提供其他信息
```java
ServiceConfig<UserService> serviceConfig = new ServiceConfig<>(UserService.class);
contextManager.registerService(new UserServiceImpl(), serviceConfig);

```

客户端同样初始化上下文，但不需要open服务器
```java
@Application(direct = true)
public class Main {
    public static void main(String[] args) throws Exception {
        ContextManager contextManager = ContextManager.build(Main.class);
        //客户端配置
        ReferenceConfig<?> referenceConfig = new ReferenceConfig<>(UserService.class);
        referenceConfig.setConnections(2);
        //获取代理实例
        UserService userService = (UserService) contextManager.getProxy(referenceConfig);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(40, 50, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(512),
                new ThreadPoolExecutor.DiscardPolicy());
        while (true) {
            executor.execute(() -> {
                User user = userService.getUser(1L);
                System.out.println(user);
            });
        }
    }
}
```
同样也可以不依赖注解启动
```java
public class Main {

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
```
## todolist:
- 实现zookeeper注册中心
- 实现服务分组功能
- 实现超时重试策略
- 实现快速失败策略
- 实现常见的负载均衡算法
- 实现Filter
- 支持异步调用
- 添加log信息
- 支持JSON-RPC