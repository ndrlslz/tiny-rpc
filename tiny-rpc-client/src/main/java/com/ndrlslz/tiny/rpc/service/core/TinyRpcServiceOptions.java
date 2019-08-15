package com.ndrlslz.tiny.rpc.service.core;

public class TinyRpcServiceOptions {
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    private static final int DEFAULT_MIN_CONNECTION_COUNT = 10;
    private static final int DEFAULT_MAX_CONNECTION_COUNT = 50;
    private Integer timeout = DEFAULT_TIMEOUT_SECONDS;
    private Integer minConnectionCount = DEFAULT_MIN_CONNECTION_COUNT;
    private Integer maxConnectionCount = DEFAULT_MAX_CONNECTION_COUNT;

    public TinyRpcServiceOptions withTimeout(int seconds) {
        if (seconds > 0) {
            this.timeout = seconds;
        }
        return this;
    }

    public TinyRpcServiceOptions withMinConnectionCount(int minConnectionCount) {
        if (minConnectionCount > 0) {
            this.minConnectionCount = minConnectionCount;
        }
        return this;
    }

    public TinyRpcServiceOptions withMaxConnectionCount(int maxConnectionCount) {
        if (maxConnectionCount > 0) {
            this.maxConnectionCount = maxConnectionCount;
        }
        return this;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public Integer getMinConnectionCount() {
        return minConnectionCount;
    }

    public Integer getMaxConnectionCount() {
        return maxConnectionCount;
    }
}
