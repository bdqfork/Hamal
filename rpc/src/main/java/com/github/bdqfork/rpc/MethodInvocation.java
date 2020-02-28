package com.github.bdqfork.rpc;

import java.io.Serializable;

/**
 * 远程执行接口的信息
 *
 * @author bdq
 * @since 2020/2/25
 */
public class MethodInvocation implements Serializable {
    /**
     * 接口名称
     */
    private String interfaceName;
    /**
     * 目标方法名
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] argumentTypes;
    /**
     * 参数
     */
    private Object[] arguments;

    public MethodInvocation(String interfaceName, String methodName, Class<?>[] argumentTypes, Object[] arguments) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.argumentTypes = argumentTypes;
        this.arguments = arguments;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getArgumentTypes() {
        return argumentTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
