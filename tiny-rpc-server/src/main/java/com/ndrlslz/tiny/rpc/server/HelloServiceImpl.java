package com.ndrlslz.tiny.rpc.server;

public class HelloServiceImpl implements HelloService {
    @Override
    public String say(String name) {
        return "Hello " + name;
    }
}
