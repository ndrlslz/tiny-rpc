package com.ndrlslz.tiny.rpc.core;

import java.util.List;
import java.util.concurrent.Future;

public interface HelloService<T> {
    String hello();

    String say(String name);

    String runtimeException(String name);

    String checkedException(String name) throws CheckedException;

    String uncheckedException(String name);

    Output handle(Input input);

    List<String> handle(List<T> list);

    String nullMethod();

    Future<String> helloAsync();

    Future<String> sayAsync(String name);

    Future<String> runtimeExceptionAsync(String name);

    Future<String> checkedExceptionAsync(String name);

    Future<String> uncheckedExceptionAsync(String name);

    Future<Output> handleAsync(Input input);

    Future<List<String>> handleAsync(List<T> list);

    Future<String> nullAsyncMethod();
}
