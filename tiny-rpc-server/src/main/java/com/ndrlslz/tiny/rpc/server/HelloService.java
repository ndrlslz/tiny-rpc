package com.ndrlslz.tiny.rpc.server;

public interface HelloService {
    String hello();

    String say(String name);

    String exception(String name);

    Output handle(Input input);
}
