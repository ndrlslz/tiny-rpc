package com.ndrlslz.tiny.rpc.core;

import java.util.concurrent.Future;

public interface HelloServiceAsync {
    Future<String> hello();
}
