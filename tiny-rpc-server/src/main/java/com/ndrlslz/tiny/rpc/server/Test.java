package com.ndrlslz.tiny.rpc.server;

import com.ndrlslz.tiny.rpc.server.core.TinyRpcServer;

public class Test {
    public static void main(String[] args) {
        TinyRpcServer
                .create()
                .registerService(new GreetingService())
                .listen(8888);


    }
}
