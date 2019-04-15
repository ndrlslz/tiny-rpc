package com.ndrlslz.tiny.rpc.server.test;

import com.ndrlslz.tiny.rpc.server.client.RpcTestClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RpcIntegrationTest extends IntegrationTestBase {
    private RpcTestClient rpcClient;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        rpcClient = new RpcTestClient("localhost", 6666);
    }

    @Override
    @After
    public void tearDown() {
        super.tearDown();
        rpcClient.close();
    }

    @Test
    public void shouldStartRpcServer() {
        rpcClient.send("Tom");
        String response = rpcClient.receive();
        assertThat(response, is("hello Tom"));
    }

//    @Test
//    public void shouldSendMessageMultipleTimes() {
//        rpcClient.send("Tom");
//        String response = rpcClient.receive();
//        assertThat(response, is("hello Tom"));
//
//        rpcClient.send("Jack");
//        String response1 = rpcClient.receive();
//        assertThat(response1, is("hello Jack"));
//    }
}
