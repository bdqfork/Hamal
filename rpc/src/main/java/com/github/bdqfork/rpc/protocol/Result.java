package com.github.bdqfork.rpc.protocol;

import java.io.Serializable;

/**
 * @author bdq
 * @since 2020/3/1
 */
public class Result implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2931385307864821352L;
    /**
     * 返回值类型
     */
    private Class<?> dateType;
    /**
     * 返回值
     */
    private Object data;
    /**
     * 异常
     */
    private Throwable throwable;

    public Result() {
    }

    public Result(Class<?> dateType, Object data) {
        this.dateType = dateType;
        this.data = data;
    }

    public Result(Throwable throwable) {
        this.throwable = throwable;
    }

    public Class<?> getDateType() {
        return dateType;
    }

    public void setDateType(Class<?> dateType) {
        this.dateType = dateType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
