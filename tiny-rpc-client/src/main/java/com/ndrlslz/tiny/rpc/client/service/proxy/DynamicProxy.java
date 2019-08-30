package com.ndrlslz.tiny.rpc.client.service.proxy;

import com.ndrlslz.tiny.rpc.client.core.TinyRpcClient;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.concurrent.*;

import static java.util.Objects.isNull;

public class DynamicProxy {
    private static final int CORE_POOL_SIZE = 16;
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final long KEEP_ALIVE_TIME = 60L;
    private volatile ExecutorService threadPool;
    private TinyRpcClient tinyRpcClient;

    public DynamicProxy(TinyRpcClient tinyRpcClient) {
        this.tinyRpcClient = tinyRpcClient;
    }

    public Object proxy(Class clazz) {
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                (proxy, method, args) -> {
                    if (method.getReturnType().equals(Future.class)) {
                        initThreadPool();

                        return threadPool.submit(() -> remoteInvoke(method, args));
                    } else {
                        return remoteInvoke(method, args);
                    }
                });
    }

    private void initThreadPool() {
        if (isNull(threadPool)) {
            synchronized (this) {
                if (isNull(threadPool)) {
                    threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                            KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
                }
            }
        }
    }

    public void closeThreadPool() {
        if (Objects.nonNull(threadPool)) {
            threadPool.shutdown();
        }
    }

    private Object remoteInvoke(Method method, Object[] args) throws Exception {
        Object result = tinyRpcClient.invoke(method.getName(), args);

        if (result instanceof Exception) {
            throw (Exception) result;
        }

        return result;
    }
}