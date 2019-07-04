package com.ndrlslz.tiny.rpc.service.proxy;

import com.ndrlslz.tiny.rpc.client.core.TinyRpcClient;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DynamicProxy {
    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    public static Object proxy(Class clazz, TinyRpcClient tinyRpcClient) {
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                (proxy, method, args) -> {
                    if (method.getReturnType().equals(Future.class)) {
                        return threadPool.submit(() -> remoteInvoke(tinyRpcClient, method, args));
                    } else {
                        return remoteInvoke(tinyRpcClient, method, args);
                    }
                });
    }

    private static Object remoteInvoke(TinyRpcClient tinyRpcClient, Method method, Object[] args) throws Exception {
        Object result = tinyRpcClient.invoke(method.getName(), args);

        if (result instanceof Exception) {
            throw (Exception) result;
        }

        return result;
    }
}