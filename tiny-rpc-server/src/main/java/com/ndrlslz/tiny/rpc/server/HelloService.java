package com.ndrlslz.tiny.rpc.server;

public interface HelloService {
    String say(String name);

    String exception(String name);
}
