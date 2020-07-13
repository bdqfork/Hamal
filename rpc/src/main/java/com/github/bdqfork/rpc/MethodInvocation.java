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
     *
     */
    private static final long serialVersionUID = -9169729641171647869L;
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
    /**
     * 返回值类型
     */
    private Class<?> returnType;

    public MethodInvocation() {
    }

    public MethodInvocation(String interfaceName, String methodName, Class<?>[] argumentTypes, Object[] arguments,
            Class<?> returnType) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.argumentTypes = argumentTypes;
        this.arguments = arguments;
        this.returnType = returnType;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getArgumentTypes() {
        return argumentTypes;
    }

    public void setArgumentTypes(Class<?>[] argumentTypes) {
        this.argumentTypes = argumentTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }
}
