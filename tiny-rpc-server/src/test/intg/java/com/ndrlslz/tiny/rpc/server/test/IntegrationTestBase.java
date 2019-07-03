package com.ndrlslz.tiny.rpc.server.test;

import com.ndrlslz.tiny.rpc.core.HelloServiceImpl;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcRequest;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcResponse;
import com.ndrlslz.tiny.rpc.core.serialization.HessianSerializer;
import com.ndrlslz.tiny.rpc.server.client.RpcTestClient;
import com.ndrlslz.tiny.rpc.server.core.TinyRpcServer;
import com.ndrlslz.tiny.rpc.server.core.TinyRpcServerOptions;
import org.junit.After;
import org.junit.Before;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class IntegrationTestBase {
    private static final HessianSerializer HESSIAN_SERIALIZER = new HessianSerializer();
    private TinyRpcServer tinyRpcServer;
    private RpcTestClient rpcClient;

    @Before
    public void setUp() {
        tinyRpcServer = TinyRpcServer.create(new TinyRpcServerOptions().withWorkerThreadCount(20))
                .registerService(new HelloServiceImpl<String>())
                .listen(6666);

        rpcClient = new RpcTestClient("localhost", 6666);
    }

    @After
    public void tearDown() {
        tinyRpcServer.close();
        rpcClient.close();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    Object callRemoteMethod(String methodName, Object... arguments) {
        byte[] bytes = generateRequest(methodName, arguments);

        rpcClient.sendRpcRequest(bytes);
        byte[] bytesResponse = rpcClient.receiveRpcResponse();

        return generateResponse(bytesResponse).getResponseValue();
    }

    Object callRemoteMethodWithWrongMagicNumber(String methodName, Object... arguments) {

        byte[] bytes = generateRequest(methodName, arguments);

        rpcClient.sendRpcRequestWithWrongMagicNumber(bytes);
        byte[] bytesResponse = rpcClient.receiveRpcResponse();

        return generateResponse(bytesResponse).getResponseValue();
    }

    void sendHeartbeat() {
        rpcClient.sendHeartBeat();
    }

    private byte[] generateRequest(String methodName, Object[] arguments) {
        TinyRpcRequest tinyRpcRequest = new TinyRpcRequest();

        tinyRpcRequest.setCorrelationId(UUID.randomUUID().toString());
        tinyRpcRequest.setMethodName(methodName);
        tinyRpcRequest.setArgumentsValue(arguments);
        return HESSIAN_SERIALIZER.serialize(tinyRpcRequest);
    }

    private TinyRpcResponse generateResponse(byte[] response) {
        return HESSIAN_SERIALIZER.deserialize(response, TinyRpcResponse.class);
    }
}