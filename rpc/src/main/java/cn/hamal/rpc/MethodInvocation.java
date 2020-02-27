package cn.hamal.rpc;

import java.io.Serializable;

/**
 * @author bdq
 * @since 2020/2/25
 */
public class MethodInvocation implements Serializable {
    private String interfaceName;
    private String methodName;
    private Class<?>[] argumentTypes;
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
