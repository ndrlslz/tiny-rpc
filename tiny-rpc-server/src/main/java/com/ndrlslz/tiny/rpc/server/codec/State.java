package com.ndrlslz.tiny.rpc.server.codec;

public enum State {
    MAGIC,
    TYPE,
    BODY_LENGTH,
    BODY
}
