package com.ndrlslz.tiny.rpc.server.core;

public class TinyRpcServerOptions {
    private Integer workerThreadCount;

    public TinyRpcServerOptions withWorkerThreadCount(int count) {
        this.workerThreadCount = count;
        return this;
    }

    public Integer getWorkerThreadCount() {
        return workerThreadCount;
    }

}
