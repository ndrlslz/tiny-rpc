package com.ndrlslz.tiny.rpc.server.test;

import com.ndrlslz.tiny.rpc.server.client.RpcTestClient;
import com.ndrlslz.tiny.rpc.server.exception.TinyRpcServerException;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcRequest;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcResponse;
import com.ndrlslz.tiny.rpc.server.serialization.HessianSerializer;
import com.ndrlslz.tiny.rpc.server.service.HelloServiceImpl;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.instanceOf;
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
    public void shouldSendTinyRecRequest() {
        TinyRpcRequest tinyRpcRequest = new TinyRpcRequest();

        tinyRpcRequest.setCorrelationId(UUID.randomUUID().toString());
        tinyRpcRequest.setMethodName("say");
        tinyRpcRequest.setArgumentsValue(new Object[]{"Tom"});

        HessianSerializer hessianSerializer = new HessianSerializer();
        byte[] bytes = hessianSerializer.serialize(tinyRpcRequest);

        rpcClient.sendRpcRequest(bytes);
        byte[] response = rpcClient.receiveRpcResponse();

        TinyRpcResponse tinyRpcResponse = (TinyRpcResponse) hessianSerializer.deserialize(response);
        String result = String.valueOf(tinyRpcResponse.getResponseValue());

        assertThat(result, is("Hello Tom"));
    }

    @Test
    public void shouldGetExceptionResponse() {
        TinyRpcRequest tinyRpcRequest = new TinyRpcRequest();

        tinyRpcRequest.setCorrelationId(UUID.randomUUID().toString());
        tinyRpcRequest.setMethodName("exception");
        tinyRpcRequest.setArgumentsValue(new Object[]{"Tom"});

        HessianSerializer hessianSerializer = new HessianSerializer();
        byte[] bytes = hessianSerializer.serialize(tinyRpcRequest);

        rpcClient.sendRpcRequest(bytes);
        byte[] response = rpcClient.receiveRpcResponse();

        TinyRpcResponse tinyRpcResponse = (TinyRpcResponse) hessianSerializer.deserialize(response);
        Object responseValue = tinyRpcResponse.getResponseValue();

        assertThat(responseValue, instanceOf(TinyRpcServerException.class));
        TinyRpcServerException exception = (TinyRpcServerException) responseValue;

        assertThat(exception.getMessage(), is("Exception happened"));
    }
}
