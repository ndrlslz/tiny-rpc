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


        int count = 10000;

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(count);
//        ExecutorService threadPool = new ThreadPoolExecutor(150, 150,
//                0L, TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<Runnable>());

        ExecutorService threadPool = Executors.newFixedThreadPool(128);
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

//        TimeUnit.SECONDS.sleep(10);
        threadPool.shutdown();
        tinyRpcService.close();
    }
}
