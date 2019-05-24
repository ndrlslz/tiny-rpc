package com.ndrlslz.tiny.rpc.core.codec;

public enum State {
    MAGIC,
    TYPE,
    BODY_LENGTH,
    BODY
}
