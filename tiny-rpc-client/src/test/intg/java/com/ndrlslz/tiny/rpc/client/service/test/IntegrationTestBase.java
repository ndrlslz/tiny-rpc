package com.ndrlslz.tiny.rpc.client.service.test;

import com.ndrlslz.tiny.rpc.client.implementation.HelloService;
import com.ndrlslz.tiny.rpc.client.implementation.HelloServiceImpl;
import com.ndrlslz.tiny.rpc.server.core.TinyRpcServer;
import com.ndrlslz.tiny.rpc.server.core.TinyRpcServerOptions;
import com.ndrlslz.tiny.rpc.client.service.core.TinyRpcService;
import com.ndrlslz.tiny.rpc.client.service.core.TinyRpcServiceOptions;
import org.junit.After;
import org.junit.Before;

import java.util.concurrent.TimeUnit;

public class IntegrationTestBase {
    private TinyRpcServer tinyRpcServer;
    private TinyRpcService tinyRpcService;
    HelloService<String> helloService;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        tinyRpcServer = TinyRpcServer.create(new TinyRpcServerOptions().withWorkerThreadCount(20))
                .registerService(new HelloServiceImpl<String>())
                .listen(6666);

        tinyRpcService = TinyRpcService.create(new TinyRpcServiceOptions()
                .withTimeout(10)
                .withMinConnectionCount(10)
                .withMaxConnectionCount(100));
        helloService = (HelloService<String>) tinyRpcService
                .service(HelloService.class)
                .server("localhost", 6666);
    }

    @After
    public void tearDown() {
        tinyRpcService.close();
        tinyRpcServer.close();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
