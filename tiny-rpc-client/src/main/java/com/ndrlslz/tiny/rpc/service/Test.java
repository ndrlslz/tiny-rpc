package com.ndrlslz.tiny.rpc.service;

import com.ndrlslz.tiny.rpc.core.HelloService;
import com.ndrlslz.tiny.rpc.service.core.TinyRpcService;
import com.ndrlslz.tiny.rpc.service.core.TinyRpcServiceOptions;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Test {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        TinyRpcService tinyRpcService = TinyRpcService.create();
        HelloService service = (HelloService) tinyRpcService
                .service(HelloService.class)
                .server("localhost", 8888);


        service.hello();
        Future future = service.helloAsync();
        Future future1 = service.helloAsync();

        StopWatch stopWatch = new StopWatch();
        TimeUnit.SECONDS.sleep(4);
        stopWatch.start();

        Object result = future.get();
        Object result1 = future1.get();

        stopWatch.stop();
        System.out.println("time:" + stopWatch.getTime(TimeUnit.SECONDS));
        System.out.println(result);
        System.out.println(result1);
        tinyRpcService.close();
    }
}
