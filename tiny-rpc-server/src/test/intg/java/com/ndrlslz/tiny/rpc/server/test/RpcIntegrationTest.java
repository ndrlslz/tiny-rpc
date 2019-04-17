package com.ndrlslz.tiny.rpc.server.test;

import com.ndrlslz.tiny.rpc.server.client.RpcTestClient;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcRequest;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcResponse;
import com.ndrlslz.tiny.rpc.server.serialization.HessianSerializer;
import com.ndrlslz.tiny.rpc.server.service.HelloServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

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
    public void shouldDeserilizeTinyRpcRequest() throws NoSuchMethodException, InterruptedException {
        TinyRpcRequest tinyRpcRequest = new TinyRpcRequest();

        HelloServiceImpl helloService = new HelloServiceImpl();
        Method sayMethod = helloService.getClass().getDeclaredMethod("say", String.class);

        tinyRpcRequest.setMethodName(sayMethod.getName());
        tinyRpcRequest.setArgumentsCount(1);
        tinyRpcRequest.setArgumentsValue(new Object[]{"Tom"});

        HessianSerializer hessianSerializer = new HessianSerializer();
        byte[] bytes = hessianSerializer.serialize(tinyRpcRequest);

        rpcClient.sendRpcRequest(bytes);
        byte[] response = rpcClient.receiveRpcResponse();

        TinyRpcResponse tinyRpcResponse = (TinyRpcResponse) hessianSerializer.deserialize(response);
        String result = String.valueOf(tinyRpcResponse.getResponseValue());

        assertThat(result, is("Hello Tom"));
    }

//    @Test
//    public void shouldStartRpcServer() {
//        rpcClient.send("Tom");
//        String response = rpcClient.receive();
//        assertThat(response, is("hello Tom"));
//    }
//
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
