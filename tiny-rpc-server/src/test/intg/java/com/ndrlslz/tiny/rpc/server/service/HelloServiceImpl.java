package com.ndrlslz.tiny.rpc.server.service;

public class HelloServiceImpl implements HelloService {
    @Override
    public String say(String name) {
        return "Hello " + name;
    }

    @Override
    public String exception(String name) {
        throw new RuntimeException("Exception happened");
    }
}