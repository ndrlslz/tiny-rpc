package com.ndrlslz.tiny.rpc.client.exception;

import com.ndrlslz.tiny.rpc.core.exception.TinyRpcException;

public class TinyRpcNoAvailableConnectionException extends TinyRpcException {
    public TinyRpcNoAvailableConnectionException(String message) {
        super(message);
    }
}
