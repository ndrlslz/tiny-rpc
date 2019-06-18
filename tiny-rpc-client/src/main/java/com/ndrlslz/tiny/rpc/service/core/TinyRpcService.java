package com.ndrlslz.tiny.rpc.service.core;

import com.ndrlslz.tiny.rpc.client.core.TinyRpcClient;
import com.ndrlslz.tiny.rpc.service.proxy.DynamicProxy;

public class TinyRpcService {
    private Class serviceInterface;
    private TinyRpcClient tinyRpcClient;

    private TinyRpcService() {

    }

    public static TinyRpcService create() {
        return new TinyRpcService();
    }

    public TinyRpcService service(Class serviceInterface) {
        this.serviceInterface = serviceInterface;

        return this;
    }

    public Object server(String host, int port) {
        this.tinyRpcClient = new TinyRpcClient(host, port);

        return DynamicProxy.proxy(serviceInterface, tinyRpcClient);
    }
}