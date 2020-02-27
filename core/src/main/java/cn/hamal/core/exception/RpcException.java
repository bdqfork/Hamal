package cn.hamal.core.exception;

/**
 * @author bdq
 * @since 2020/2/27
 */
public class RpcException extends Exception {
    public RpcException() {
        super();
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }
}