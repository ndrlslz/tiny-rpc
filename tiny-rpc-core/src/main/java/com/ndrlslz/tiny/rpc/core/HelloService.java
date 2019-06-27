package com.ndrlslz.tiny.rpc.core;

import java.util.List;
import java.util.concurrent.Future;

public interface HelloService<T> {
    Future<String> helloAsync();

    String hello();

    String say(String name);

    String runtimeException(String name);

    String checkedException(String name) throws CheckedException;

    String uncheckedException(String name);

    Output handle(Input input);

    List<String> handle(List<T> list);
}
