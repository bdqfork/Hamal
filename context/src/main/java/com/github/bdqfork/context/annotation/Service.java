package com.github.bdqfork.context.annotation;

import com.github.bdqfork.core.constant.ProtocolProperty;

import java.lang.annotation.*;

/**
 * @author bdq
 * @since 2020/2/27
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Service {
    /**
     * service名称
     */
    String value() default "";

    /**
     * 服务接口
     */
    Class<?> serviceInterface();

    /**
     * 服务版本
     */
    String version() default "";

    /**
     * 分组
     */
    String group() default ProtocolProperty.DEFAULT_GROUP;

    /**
     * 超时时间
     */
    long timeout() default ProtocolProperty.DEFAULT_TIMEOUT;
}
