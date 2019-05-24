package com.ndrlslz.tiny.rpc.server.test;

import com.ndrlslz.tiny.rpc.server.Details;
import com.ndrlslz.tiny.rpc.server.Input;
import com.ndrlslz.tiny.rpc.server.Output;
import com.ndrlslz.tiny.rpc.server.client.RpcTestClient;
import com.ndrlslz.tiny.rpc.server.exception.TinyRpcServerException;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcRequest;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcResponse;
import com.ndrlslz.tiny.rpc.server.serialization.HessianSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class RpcIntegrationTest extends IntegrationTestBase {
    private RpcTestClient rpcClient;
    private static final HessianSerializer HESSIAN_SERIALIZER = new HessianSerializer();

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
    public void should_call_hello_world_method() {
        Object response = callRemoteMethod("hello");

        assertThat(response, instanceOf(String.class));
        assertThat(String.valueOf(response), is("hello world"));
    }

    @Test
    public void should_call_method_with_string_argument() {
        Object response = callRemoteMethod("say", "Tom");

        assertThat(response, instanceOf(String.class));
        assertThat(String.valueOf(response), is("Hello Tom"));
    }

    @Test
    public void should_call_method_with_object_argument() {
        Details details = new Details();
        details.setAge(22);
        details.setAddress("chengdu");

        Input input = new Input();
        input.setName("Tom");
        input.setDetails(details);

        Object response = callRemoteMethod("handle", input);

        assertThat(response, instanceOf(Output.class));
        assertThat(((Output) response).getMessage(), is("Tom_chengdu_22"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_call_method_with_paradigm() {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        Object response = callRemoteMethod("handle", list);
        assertThat(response, instanceOf(List.class));

        List<String> result = (List<String>) response;
        assertThat(result.size(), is(3));
        assertThat(result, hasItems("1@", "2@", "3@"));
    }

    @Test
    public void should_get_magic_number_not_correct_exception() {
        Object responseValue = callRemoteMethodWithWrongMagicNumber("hello");

        assertThat(responseValue, instanceOf(TinyRpcServerException.class));
        assertThat(((TinyRpcServerException) responseValue).getMessage(), is("Magic number is not correct"));
    }

    @Test
    public void should_get_exception_response_when_method_throw_exception() {
        Object responseValue = callRemoteMethod("exception", "Tom");

        assertThat(responseValue, instanceOf(TinyRpcServerException.class));
        assertThat(((TinyRpcServerException) responseValue).getMessage(), is("Exception happened"));
    }

    @Test
    public void should_get_exception_response_when_method_not_match() {
        Object responseValue = callRemoteMethod("say");

        assertThat(responseValue, instanceOf(TinyRpcServerException.class));
        assertThat(((TinyRpcServerException) responseValue).getMessage(), containsString("No such accessible method: say()"));
    }

    @Test
    public void should_send_heartbeat() {
        sendHeartbeat();
        sendHeartbeat();
        sendHeartbeat();
    }

    private Object callRemoteMethod(String methodName, Object... arguments) {
        byte[] bytes = generateRequest(methodName, arguments);

        rpcClient.sendRpcRequest(bytes);
        byte[] bytesResponse = rpcClient.receiveRpcResponse();

        return generateResponse(bytesResponse).getResponseValue();
    }

    private Object callRemoteMethodWithWrongMagicNumber(String methodName, Object... arguments) {

        byte[] bytes = generateRequest(methodName, arguments);

        rpcClient.sendRpcRequestWithWrongMagicNumber(bytes);
        byte[] bytesResponse = rpcClient.receiveRpcResponse();

        return generateResponse(bytesResponse).getResponseValue();
    }

    private void sendHeartbeat() {
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