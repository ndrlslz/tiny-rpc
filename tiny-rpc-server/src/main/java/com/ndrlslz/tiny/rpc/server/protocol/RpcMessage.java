package com.ndrlslz.tiny.rpc.server.protocol;

public class RpcMessage<T> {
    private Integer bodyLength;
    private T body;
}
