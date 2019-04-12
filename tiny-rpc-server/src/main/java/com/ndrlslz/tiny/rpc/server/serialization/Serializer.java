package com.ndrlslz.tiny.rpc.server.serialization;

public interface Serializer {
    byte[] serialize(Object object);

    Object deserialize(byte[] bytes);
}
