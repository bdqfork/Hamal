package com.github.bdqfork.rpc.protocol;

import java.io.Serializable;

/**
 * 响应信息
 *
 * @author bdq
 * @since 2020/2/25
 */
public class Response implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1098331013831167056L;
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
    private int status = Request.OK;
    /**
     * 请求数据
     */
    private Object payload;

    public Response(){}

    public Response(Long id) {
        this.id = id;
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
