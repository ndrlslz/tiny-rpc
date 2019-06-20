package com.ndrlslz.tiny.rpc.service.core;

public class TinyRpcServiceOptions {
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    private Integer timeout = DEFAULT_TIMEOUT_SECONDS;

    public TinyRpcServiceOptions withTimeout(int seconds) {
        this.timeout = seconds;
        return this;
    }

    public Integer getTimeout() {
        return timeout;
    }
}
