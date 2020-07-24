package com.github.bdqfork.context.annotation;


import java.lang.annotation.*;

/**
 * 描述应用信息
 *
 * @author bdq
 * @since 2020/2/27
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Application {

    /**
     * 协议
     */
    String protocol() default "rpc";

    /**
     * 服务端类型
     */
    String server() default "netty";

    /**
     * 序列化方式
     */
    String serilizer() default "hessian";

    /**
     * 服务端地址
     */
    String host() default "127.0.0.1";

    /**
     * 服务器端口
     */
    int port() default 8080;

    /**
     * 负载均衡算法
     */
    String loadbalancer() default "random";

    /**
     * 是否直接端到端调用服务，而不使用注册中心
     */
    boolean direct() default false;

    /**
     * 指定服务容器
     */
    String container() default "rpc";
}
