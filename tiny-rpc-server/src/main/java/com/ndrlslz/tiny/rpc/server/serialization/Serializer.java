package com.ndrlslz.tiny.rpc.server.serialization;

public interface Serializer {
    <T> byte[] serialize(T object);

    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
