package com.github.bdqfork.core.exception;

/**
 * @author bdq
 * @since 2020/7/28
 */
public class RegistryException extends RuntimeException {
    public RegistryException(String message) {
        super(message);
    }

    public RegistryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistryException(Throwable cause) {
        super(cause);
    }

    protected RegistryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
