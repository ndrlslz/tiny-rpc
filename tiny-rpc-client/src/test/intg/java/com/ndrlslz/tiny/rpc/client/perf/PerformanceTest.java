package com.ndrlslz.tiny.rpc.client.perf;

import com.ndrlslz.tiny.rpc.core.HelloService;
import com.ndrlslz.tiny.rpc.core.HelloServiceImpl;
import com.ndrlslz.tiny.rpc.server.core.TinyRpcServer;
import com.ndrlslz.tiny.rpc.client.service.core.TinyRpcService;
import com.ndrlslz.tiny.rpc.client.service.core.TinyRpcServiceOptions;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class PerformanceTest {
    public static void main(String[] args) throws InterruptedException {
        int port = 8888;
        TinyRpcServer tinyRpcServer = TinyRpcServer
                .create()
                .registerService(new HelloServiceImpl())
                .listen(port);

        TinyRpcService tinyRpcService = TinyRpcService.create(new TinyRpcServiceOptions()
                .withTimeout(10)
                .withMinConnectionCount(10)
                .withMaxConnectionCount(100));

        HelloService service = (HelloService) tinyRpcService
                .service(HelloService.class)
                .server("localhost", port);

        service.hello();

        ExecutorService threadPool = Executors.newFixedThreadPool(1024);

        run(service, threadPool);

        threadPool.shutdown();
        tinyRpcService.close();
        tinyRpcServer.close();
    }

    private static void run(HelloService service, ExecutorService threadPool) throws InterruptedException {
        int count = 100000;

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(count);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        IntStream.range(0, count).forEach(index -> threadPool.submit(() -> {
            start.await();
            try {
                return service.hello();
            } catch (Exception e) {
                System.out.println(e.getClass());
                e.printStackTrace();
            } finally {
                finish.countDown();
            }
            return null;
        }));
        start.countDown();
        finish.await();

        stopWatch.stop();
        System.out.println("Time: " + stopWatch.getTime(TimeUnit.MILLISECONDS));
    }
}