package com.ndrlslz.tiny.rpc.client;

import com.ndrlslz.tiny.rpc.client.core.TinyRpcClient;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Test {
    public static void main(String[] args) throws Exception {
        TinyRpcClient tinyRpcClient = new TinyRpcClient("localhost", 8888);
        StopWatch watch = new StopWatch();
        watch.start();
        IntStream.range(0, 1).forEach(value -> {
            try {
                System.out.println(tinyRpcClient.send("say", new Object[]{String.valueOf(value)}));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        watch.stop();


        Object hello = tinyRpcClient.send("hello", new Object[]{});
        System.out.println(hello);

        Object exception = tinyRpcClient.send("runtimeException", new Object[]{"tom"});
        System.out.println(exception);

//        tinyRpcClient.send()
        System.out.println(watch.getTime(TimeUnit.SECONDS));
        tinyRpcClient.close();
    }
}
