package com.ndrlslz.tiny.rpc.client.exception;

import com.ndrlslz.tiny.rpc.core.exception.TinyRpcException;

public class TinyRpcTimeoutException extends TinyRpcException {
    public TinyRpcTimeoutException(String message) {
        super(message);
    }
}
