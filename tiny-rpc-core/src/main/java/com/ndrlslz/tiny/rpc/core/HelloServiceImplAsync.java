package com.ndrlslz.tiny.rpc.core;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class HelloServiceImplAsync implements HelloServiceAsync {
    @Override
    public Future<String> hello() {
        return CompletableFuture.supplyAsync(() -> "test");
    }
}
