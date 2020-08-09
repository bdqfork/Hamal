package com.github.bdqfork.rpc.protocol;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 请求信息
 *
 * @author bdq
 * @since 2020/2/25
 */
public class Request implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6866116312970066166L;
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    public static final int OK = 200;
    public static final int ERROR = 500;
    public static final byte HEART_BEAT = 1;
    /**
     * 请求id
     */
    private Long id;
    /**
     * 请求类型
     */
    private byte event;
    /**
     * 请求状态
     */
    private int status = OK;
    /**
     * 请求数据
     */
    private Object payload;

    public Request() {}

    public Request(Long id) {
        this.id = id;
    }

    public static Long newId() {
        return ID_GENERATOR.getAndIncrement();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte getEvent() {
        return event;
    }

    public void setEvent(byte event) {
        this.event = event;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

}
