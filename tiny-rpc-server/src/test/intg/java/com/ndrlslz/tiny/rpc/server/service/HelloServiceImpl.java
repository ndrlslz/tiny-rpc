package com.ndrlslz.tiny.rpc.server.service;

public class HelloServiceImpl implements HelloService {
    @Override
    public String say(String name) {
        return "Hello " + name;
    }
}
