package com.ndrlslz.tiny.rpc.service;

import com.ndrlslz.tiny.rpc.core.HelloService;
import com.ndrlslz.tiny.rpc.service.core.TinyRpcService;
import com.ndrlslz.tiny.rpc.service.core.TinyRpcServiceOptions;
import org.apache.commons.lang3.time.StopWatch;

import java.util.stream.IntStream;

public class Test {
    public static void main(String[] args) {

        TinyRpcService tinyRpcService = TinyRpcService.create();
        HelloService service = (HelloService) tinyRpcService
                .service(HelloService.class)
                .server("localhost", 8888);

        String hello = service.hello();
        System.out.println(hello);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        IntStream.range(0, 10000).forEach(index -> {
            System.out.println(service.hello());
        });

        stopWatch.stop();
        long time = stopWatch.getTime();
        System.out.println(time);
        tinyRpcService.close();
    }
}
