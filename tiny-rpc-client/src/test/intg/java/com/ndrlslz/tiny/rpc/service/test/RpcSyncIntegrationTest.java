package com.ndrlslz.tiny.rpc.service.test;

import com.ndrlslz.tiny.rpc.core.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class RpcSyncIntegrationTest extends IntegrationTestBase {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void should_call_hello_world_method() {
        String hello = helloService.hello();

        assertThat(hello, is("hello world"));
    }

    @Test
    public void should_call_method_with_string_argument() {
        String tom = helloService.say("Tom");

        assertThat(tom, is("Hello Tom"));
    }

    @Test
    public void should_call_method_with_object_argument() {
        Details details = new Details();
        details.setAge(22);
        details.setAddress("chengdu");

        Input input = new Input();
        input.setName("Tom");
        input.setDetails(details);

        Output output = helloService.handle(input);

        assertThat(output.getMessage(), is("Tom_chengdu_22"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_call_method_with_paradigm() {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        List<String> result = helloService.handle(list);

        assertThat(result.size(), is(3));
        assertThat(result, hasItems("1@", "2@", "3@"));
    }

    @Test
    public void should_call_null_method() {
        String result = helloService.nullMethod();

        assertThat(result, is(nullValue()));
    }

    @Test
    public void should_get_runtime_exception_response_when_method_throw_runtime_exception() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Runtime exception happened");

        helloService.runtimeException("Tom");
    }

    @Test
    public void should_get_checked_exception_response_when_method_throw_checked_exception() {
        try {
            helloService.checkedException("Tom");
        } catch (CheckedException exception) {
            assertThat(exception, instanceOf(CheckedException.class));
            assertThat(exception.getMessage(), is("Checked exception happened"));

        }
    }

    @Test
    public void should_get_unchecked_exception_when_method_throw_unchecked_exception_given_interface_and_exception_in_same_jar() {
        expectedException.expect(UncheckedException.class);
        expectedException.expectMessage("Unchecked exception happened");

        helloService.uncheckedException("Tom");
    }
}