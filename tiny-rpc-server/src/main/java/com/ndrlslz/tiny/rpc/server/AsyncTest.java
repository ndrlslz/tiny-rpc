package com.ndrlslz.tiny.rpc.server;

import com.ndrlslz.tiny.rpc.core.HelloServiceImplAsync;
import com.ndrlslz.tiny.rpc.server.core.TinyRpcServer;

public class AsyncTest {
    public static void main(String[] args) {
        TinyRpcServer
                .create()
                .registerService(new HelloServiceImplAsync())
                .listen(10088);
    }
}
