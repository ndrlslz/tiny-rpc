package com.ndrlslz.tiny.rpc.service.core;

import com.ndrlslz.tiny.rpc.client.core.TinyRpcClient;
import com.ndrlslz.tiny.rpc.client.model.NullObject;
import com.ndrlslz.tiny.rpc.service.proxy.DynamicProxy;

import java.util.concurrent.Future;

public class TinyRpcService {
    private Class serviceInterface;
    private TinyRpcClient tinyRpcClient;
    private TinyRpcServiceOptions tinyRpcServiceOptions;

    private TinyRpcService() {

    }

    public static TinyRpcService create() {
        return new TinyRpcService();
    }

    public static TinyRpcService create(TinyRpcServiceOptions options) {
        TinyRpcService tinyRpcService = create();
        tinyRpcService.tinyRpcServiceOptions = options;
        return tinyRpcService;
    }

    public TinyRpcService service(Class serviceInterface) {
        this.serviceInterface = serviceInterface;

        return this;
    }

    public Object server(String host, int port) {
        this.tinyRpcClient = new TinyRpcClient(host, port, tinyRpcServiceOptions);

        return DynamicProxy.proxy(serviceInterface, tinyRpcClient);
    }

    public void close() {
        tinyRpcClient.close();
    }
}