package com.ndrlslz.tiny.rpc;

import com.ndrlslz.tiny.rpc.core.HelloService;
import com.ndrlslz.tiny.rpc.service.core.TinyRpcService;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class PerfTest {
    public static void main(String[] args) throws InterruptedException {
        TinyRpcService tinyRpcService = TinyRpcService.create();
        HelloService service = (HelloService) tinyRpcService
                .service(HelloService.class)
                .server("localhost", 8888);

        service.hello();

        ExecutorService threadPool = Executors.newFixedThreadPool(1024);

        run(service, threadPool);

//        TimeUnit.SECONDS.sleep(20);
//
//        run(service, threadPool);

        threadPool.shutdown();
        tinyRpcService.close();
    }

    private static void run(HelloService service, ExecutorService threadPool) throws InterruptedException {
        int count = 100000;

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(count);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        IntStream.range(0, count).forEach(index -> {
            threadPool.submit(new Callable<String>() {
                @Override
                public String call() throws InterruptedException {
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
                }
            });
        });
        start.countDown();
        finish.await();

        stopWatch.stop();
        System.out.println("Time: " + stopWatch.getTime(TimeUnit.MILLISECONDS));
    }
}
