package com.ndrlslz.tiny.rpc;

import com.ndrlslz.tiny.rpc.core.HelloService;
import com.ndrlslz.tiny.rpc.service.core.TinyRpcService;

import java.util.concurrent.TimeUnit;

public class JustTest {
    public static void main(String[] args) throws InterruptedException {
        TinyRpcService tinyRpcService = TinyRpcService.create();
        HelloService service = (HelloService) tinyRpcService
                .service(HelloService.class)
                .server("localhost", 8888);


        System.out.println(service.hello());

        TimeUnit.SECONDS.sleep(10);

        System.out.println(service.hello());

        TimeUnit.SECONDS.sleep(10);

        System.out.println(service.hello());

    }
}
