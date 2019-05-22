package com.ndrlslz.tiny.rpc.server;

import java.util.List;
import java.util.stream.Collectors;

public class HelloServiceImpl<T> implements HelloService<T> {

    @Override
    public String hello() {
        return "hello world";
    }

    @Override
    public String say(String name) {
        return "Hello " + name;
    }

    @Override
    public String exception(String name) {
        throw new RuntimeException("Exception happened");
    }

    @Override
    public Output handle(Input input) {
        String message = String.join("_", input.getName(),
                input.getDetails().getAddress(), String.valueOf(input.getDetails().getAge()));

        Output output = new Output();
        output.setMessage(message);

        return output;
    }

    @Override
    public List<String> handle(List<T> list) {

        return list.stream()
                .map(t -> t.toString() + "@")
                .collect(Collectors.toList());
    }
}
