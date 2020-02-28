package com.github.bdqfork.core.exception;

/**
 * @author bdq
 * @since 2020/2/27
 */
public class RemoteException extends RpcException {

    public RemoteException(String message) {
        super(message);
    }

    public RemoteException(Throwable cause) {
        super(cause);
    }
}
