package com.ndrlslz.tiny.rpc.server.test;

import com.ndrlslz.tiny.rpc.core.*;
import com.ndrlslz.tiny.rpc.core.exception.TinyRpcException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class RpcAsyncIntegrationTest extends IntegrationTestBase  {
    @Test
    public void should_call_hello_world_async_method() {
        Object response = callRemoteMethod("helloAsync");

        assertThat(response, instanceOf(String.class));
        assertThat(String.valueOf(response), is("hello world"));
    }

    @Test
    public void should_call_async_method_with_string_argument() {
        Object response = callRemoteMethod("sayAsync", "Tom");

        assertThat(response, instanceOf(String.class));
        assertThat(String.valueOf(response), is("Hello Tom"));
    }

    @Test
    public void should_call_async_method_with_object_argument() {
        Details details = new Details();
        details.setAge(22);
        details.setAddress("chengdu");

        Input input = new Input();
        input.setName("Tom");
        input.setDetails(details);

        Object response = callRemoteMethod("handleAsync", input);

        assertThat(response, instanceOf(Output.class));
        assertThat(((Output) response).getMessage(), is("Tom_chengdu_22"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_call_async_method_with_paradigm() {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        Object response = callRemoteMethod("handleAsync", list);
        assertThat(response, instanceOf(List.class));

        List<String> result = (List<String>) response;
        assertThat(result.size(), is(3));
        assertThat(result, hasItems("1@", "2@", "3@"));
    }

    @Test
    public void should_call_null_async_method() {
        Object response = callRemoteMethod("nullAsyncMethod");
        assertThat(response, is(nullValue()));
    }

    @Test
    public void should_get_magic_number_not_correct_exception() {
        Object responseValue = callRemoteMethodWithWrongMagicNumber("helloAsync");

        assertThat(responseValue, instanceOf(TinyRpcException.class));
        assertThat(((TinyRpcException) responseValue).getMessage(), is("Magic number is not correct"));
    }

    @Test
    public void should_get_runtime_exception_response_when_async_method_throw_runtime_exception() {
        Object responseValue = callRemoteMethod("runtimeExceptionAsync", "Tom");

        assertThat(responseValue, instanceOf(RuntimeException.class));
        assertThat(((RuntimeException) responseValue).getMessage(), is("Runtime exception happened"));
    }

    @Test
    public void should_get_checked_exception_response_when_async_method_throw_checked_exception() {
        Object responseValue = callRemoteMethod("checkedExceptionAsync", "Tom");

        assertThat(responseValue, instanceOf(CheckedException.class));
        assertThat(((CheckedException) responseValue).getMessage(), is("Checked exception happened"));
    }

    @Test
    public void should_get_unchecked_exception_when_async_method_throw_unchecked_exception_given_interface_and_exception_in_same_jar() {
        Object responseValue = callRemoteMethod("uncheckedExceptionAsync", "Tom");

        assertThat(responseValue, instanceOf(UncheckedException.class));
        assertThat(((UncheckedException) responseValue).getMessage(), is("Unchecked exception happened"));
    }

    @Test
    public void should_get_exception_response_when_async_method_not_match() {
        Object responseValue = callRemoteMethod("sayAsync");

        assertThat(responseValue, instanceOf(TinyRpcException.class));
        assertThat(((TinyRpcException) responseValue).getMessage(), containsString("No such accessible method: sayAsync()"));
    }
}