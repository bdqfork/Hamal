package cn.hamal.core.exception;

/**
 * @author bdq
 * @since 2020/2/25
 */
public class SerializerException extends Exception {
    public SerializerException(String message) {
        super(message);
    }

    public SerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializerException(Throwable cause) {
        super(cause);
    }
}
