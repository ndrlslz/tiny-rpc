package com.ndrlslz.tiny.rpc;

import com.ndrlslz.tiny.rpc.core.HelloService;
import com.ndrlslz.tiny.rpc.core.exception.TinyRpcException;
import com.ndrlslz.tiny.rpc.service.core.TinyRpcService;


public class JustTest {
    public static void main(String[] args) throws InterruptedException {
        TinyRpcService tinyRpcService = TinyRpcService.create();
        HelloService service = (HelloService) tinyRpcService
                .service(HelloService.class)
                .server("localhost", 8888);


        try {
            System.out.println(service.hello());
        } catch (TinyRpcException e) {
//            e.printStackTrace();
        }

        tinyRpcService.close();
    }
}
