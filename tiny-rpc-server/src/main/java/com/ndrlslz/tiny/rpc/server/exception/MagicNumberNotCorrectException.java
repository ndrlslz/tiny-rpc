package com.ndrlslz.tiny.rpc.server.exception;

public class MagicNumberNotCorrectException extends RuntimeException {
    public MagicNumberNotCorrectException(String message) {
        super(message);
    }
}
