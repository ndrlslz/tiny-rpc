package com.ndrlslz.tiny.rpc.service.proxy;

import com.ndrlslz.tiny.rpc.client.core.TinyRpcClient;

import java.lang.reflect.Proxy;

public class DynamicProxy {
    public static Object proxy(Class clazz, TinyRpcClient tinyRpcClient) {
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                (proxy, method, args) -> {
                    Object result = tinyRpcClient.invoke(method.getName(), args);

                    if (result instanceof Exception) {
                        throw (Exception) result;
                    }

                    return result;
                });
    }
}