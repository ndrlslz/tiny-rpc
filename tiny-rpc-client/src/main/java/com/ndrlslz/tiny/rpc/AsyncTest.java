package com.ndrlslz.tiny.rpc;

import com.ndrlslz.tiny.rpc.core.HelloServiceAsync;
import com.ndrlslz.tiny.rpc.service.core.TinyRpcService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        HelloServiceAsync service = (HelloServiceAsync) TinyRpcService.create()
                .service(HelloServiceAsync.class)
                .server("localhost", 10088);


        Future<String> hello = service.hello();
        System.out.println(hello.get());
    }
}
