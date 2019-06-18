package com.ndrlslz.tiny.rpc.service.proxy;

import com.ndrlslz.tiny.rpc.client.core.TinyRpcClient;

import java.lang.reflect.Proxy;

public class DynamicProxy {
    public static <T> Object proxy(Class<T> clazz, TinyRpcClient tinyRpcClient) {
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                (proxy, method, args) -> {
                    System.out.println("start to inject code");
                    Object result = tinyRpcClient.send(method.getName(), args);

                    if (result instanceof Exception) {
                        throw (Exception) result;
                    }
                    System.out.println("end to inject code");
                    return result;
                });
    }
}