package com.ndrlslz.tiny.rpc.server;

public class HelloServiceImpl implements HelloService {

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
}
