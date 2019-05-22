package com.ndrlslz.tiny.rpc.server;

import java.util.List;

public interface HelloService<T> {
    String hello();

    String say(String name);

    String exception(String name);

    Output handle(Input input);

    List<String> handle(List<T> list);
}
