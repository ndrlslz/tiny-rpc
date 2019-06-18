package com.ndrlslz.tiny.rpc.server.test;

import com.ndrlslz.tiny.rpc.core.HelloServiceImpl;
import com.ndrlslz.tiny.rpc.server.core.TinyRpcServer;
import com.ndrlslz.tiny.rpc.server.core.TinyRpcServerOptions;
import org.junit.After;
import org.junit.Before;

import java.util.concurrent.TimeUnit;

public class IntegrationTestBase {
    private TinyRpcServer tinyRpcServer;

    @Before
    public void setUp() {
        tinyRpcServer = TinyRpcServer.create(new TinyRpcServerOptions().withWorkerThreadCount(20))
                .registerService(new HelloServiceImpl<String>())
                .listen(6666);
    }

    @After
    public void tearDown() {
        tinyRpcServer.close();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}