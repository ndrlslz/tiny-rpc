package com.ndrlslz.tiny.rpc.server.protocol;

public class RpcMessage<T> {
    private Integer bodyLength;
    private T body;

    public Integer getBodyLength() {
        return bodyLength;
    }

    public T getBody() {
        return body;
    }

    public void setBodyLength(Integer bodyLength) {
        this.bodyLength = bodyLength;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
