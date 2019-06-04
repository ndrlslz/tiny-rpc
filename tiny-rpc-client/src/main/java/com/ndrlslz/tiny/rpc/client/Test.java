package com.ndrlslz.tiny.rpc.client;

import com.ndrlslz.tiny.rpc.client.core.TinyRpcClient;

public class Test {
    public static void main(String[] args) {
//        HelloWorldService service = TinyRpcService.create()
//                .service(HelloWorldService.class)
//                .server("localhost:9999");
//
//        service.hello();


//        HelloWorldServiceAsync service = TinyRpcService.create()
//                .service(HelloWorldServiceAsync.class)
//                .server("localhost:9999");
//
//        service.hello();

        TinyRpcClient tinyRpcClient = new TinyRpcClient("localhost", 8888);
    }
}
