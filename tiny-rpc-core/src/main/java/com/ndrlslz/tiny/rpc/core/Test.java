package com.ndrlslz.tiny.rpc.core;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Test {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        HelloServiceImplAsync service = new HelloServiceImplAsync();
        Future<String> hello = service.hello();

        System.out.println(hello.get());
    }
}
