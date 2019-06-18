package com.ndrlslz.tiny.rpc.core;

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
    public String runtimeException(String name) {
        throw new RuntimeException("Runtime exception happened");
    }

    @Override
    public String checkedException(String name) throws CheckedException {
        throw new CheckedException("Checked exception happened");
    }

    @Override
    public String uncheckedException(String name) {
        throw new UncheckedException("Unchecked exception happened");
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