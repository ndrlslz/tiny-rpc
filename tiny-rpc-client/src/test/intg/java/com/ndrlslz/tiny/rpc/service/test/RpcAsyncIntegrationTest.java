package com.ndrlslz.tiny.rpc.service.test;

import com.ndrlslz.tiny.rpc.core.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class RpcAsyncIntegrationTest extends IntegrationTestBase {
    @Test
    public void should_call_hello_world_async_method() throws ExecutionException, InterruptedException {
        Future<String> future = helloService.helloAsync();

        assertThat(future.get(), is("hello world"));
    }

    @Test
    public void should_call_async_method_with_string_argument() throws ExecutionException, InterruptedException {
        Future<String> future = helloService.sayAsync("Tom");

        assertThat(future.get(), is("Hello Tom"));
    }

    @Test
    public void should_call_async_method_with_object_argument() throws ExecutionException, InterruptedException {
        Details details = new Details();
        details.setAge(22);
        details.setAddress("chengdu");

        Input input = new Input();
        input.setName("Tom");
        input.setDetails(details);

        Future<Output> future = helloService.handleAsync(input);

        assertThat(future.get().getMessage(), is("Tom_chengdu_22"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_call_async_method_with_paradigm() throws ExecutionException, InterruptedException {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        Future<List<String>> future = helloService.handleAsync(list);
        List<String> result = future.get();

        assertThat(result.size(), is(3));
        assertThat(result, hasItems("1@", "2@", "3@"));
    }

    @Test
    public void should_call_null_async_method() throws ExecutionException, InterruptedException {
        Future<String> future = helloService.nullAsyncMethod();

        assertThat(future.get(), is(nullValue()));
    }

    @Test
    public void should_get_runtime_exception_response_when_async_method_throw_runtime_exception() {
        Future<String> future = helloService.runtimeExceptionAsync("Tom");
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            assertThat(e.getCause(), instanceOf(RuntimeException.class));
            assertThat(e.getCause().getMessage(), is("Runtime exception happened"));
        }
    }

    @Test
    public void should_get_checked_exception_response_when_async_method_throw_checked_exception() {
        Future<String> future = helloService.checkedExceptionAsync("Tom");

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            assertThat(e.getCause(), instanceOf(CheckedException.class));
            assertThat(e.getCause().getMessage(), is("Checked exception happened"));
        }
    }

    @Test
    public void should_get_unchecked_exception_when_async_method_throw_unchecked_exception_given_interface_and_exception_in_same_jar() {
        Future<String> future = helloService.uncheckedExceptionAsync("Tom");

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            assertThat(e.getCause(), instanceOf(UncheckedException.class));
            assertThat(e.getCause().getMessage(), is("Unchecked exception happened"));
        }
    }
}