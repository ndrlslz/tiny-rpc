package com.ndrlslz.tiny.rpc.service.proxy;

import com.ndrlslz.tiny.rpc.client.core.TinyRpcClient;

import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class DynamicProxy {
    public static Object proxy(Class clazz, TinyRpcClient tinyRpcClient) {
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                (proxy, method, args) -> {
                    if (method.getReturnType().equals(Future.class)) {
                        return CompletableFuture.supplyAsync(() -> {
                            try {
                                return tinyRpcClient.invoke(method.getName(), args);
                            } catch (Exception e) {
                                return e;
                            }
                        });
                    } else {
                        Object result = tinyRpcClient.invoke(method.getName(), args);

                        if (result instanceof Exception) {
                            throw (Exception) result;
                        }

                        return result;
                    }
                });
    }
}