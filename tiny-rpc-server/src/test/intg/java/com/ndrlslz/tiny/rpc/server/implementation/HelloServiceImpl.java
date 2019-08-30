package com.ndrlslz.tiny.rpc.server.implementation;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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

    @Override
    public String nullMethod() {
        return null;
    }

    @Override
    public Future<String> helloAsync() {
        return CompletableFuture.supplyAsync(() -> "hello world");
    }

    @Override
    public Future<String> sayAsync(String name) {
        return CompletableFuture.supplyAsync(() -> "Hello " + name);
    }

    @Override
    public Future<String> runtimeExceptionAsync(String name) {
        return CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Runtime exception happened");
        });
    }

    @Override
    public Future<String> checkedExceptionAsync(String name) {
        return Executors.newFixedThreadPool(1).submit(() -> {
            throw new CheckedException("Checked exception happened");
        });
    }

    @Override
    public Future<String> uncheckedExceptionAsync(String name) {
        return Executors.newFixedThreadPool(1).submit(() -> {
            throw new UncheckedException("Unchecked exception happened");
        });
    }

    @Override
    public Future<Output> handleAsync(Input input) {
        return CompletableFuture.supplyAsync(() -> {
            String message = String.join("_", input.getName(),
                    input.getDetails().getAddress(), String.valueOf(input.getDetails().getAge()));

            Output output = new Output();
            output.setMessage(message);

            return output;
        });
    }

    @Override
    public Future<List<String>> handleAsync(List<T> list) {
        return CompletableFuture.supplyAsync(() -> list.stream()
                .map(t -> t.toString() + "@")
                .collect(Collectors.toList()));
    }

    @Override
    public Future<String> nullAsyncMethod() {
        return CompletableFuture.supplyAsync(() -> null);
    }
}