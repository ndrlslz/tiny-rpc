package com.ndrlslz.tiny.rpc.service;

import com.ndrlslz.tiny.rpc.core.*;
import com.ndrlslz.tiny.rpc.service.core.TinyRpcService;

import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        HelloService service = (HelloService) TinyRpcService.create()
                .service(HelloService.class)
                .server("localhost", 8888);

        String hello = service.hello();
        System.out.println(hello);

        String tom = service.say("Tom");
        System.out.println(tom);

        Details details = new Details();
        details.setAge(22);
        details.setAddress("chengdu");

        Input input = new Input();
        input.setName("Tom");
        input.setDetails(details);
        Output output = service.handle(input);
        System.out.println(output.getMessage());

        List<String> strings = Arrays.asList("1", "2", "3");
        List<String> list = service.handle(strings);
        list.forEach(System.out::println);

        try {
            service.runtimeException("tom");
        } catch (RuntimeException exception) {
            System.out.println(exception.getMessage());
        }

        try {
            service.uncheckedException("tom");
        } catch (UncheckedException exception) {
            System.out.println(exception.getMessage());
        }

        try {
            service.checkedException("tom");
        } catch (CheckedException e) {
            System.out.println(e.getMessage());
        }

//        HelloWorldServiceAsync service = TinyRpcService.create()
//                .service(HelloWorldServiceAsync.class)
//                .server("localhost:9999");
//
//        service.hello();

    }
}
