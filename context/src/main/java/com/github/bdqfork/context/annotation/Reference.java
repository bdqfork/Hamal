package com.github.bdqfork.context.annotation;

import com.github.bdqfork.core.constant.ProtocolProperty;

import java.lang.annotation.*;

/**
 * @author bdq
 * @since 2020/2/26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Reference {
    /**
     * reference名称
     */
    String value() default "";

    /**
     * 服务接口名
     */
    Class<?> serviceInterface() default Object.class;

    /**
     * 服务分组
     */
    String group() default ProtocolProperty.DEFAULT_GROUP;

    /**
     * 超时时间
     */
    long timeout() default ProtocolProperty.DEFAULT_TIMEOUT;

    /**
     * 连接数量
     */
    int connections() default 1;

    /**
     * 版本
     */
    String version() default "";

}
