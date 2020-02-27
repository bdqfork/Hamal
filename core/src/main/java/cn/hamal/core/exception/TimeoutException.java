package cn.hamal.core.exception;

/**
 * @author bdq
 * @since 2020/2/25
 */
public class TimeoutException extends RpcException {
    public TimeoutException() {
    }

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
