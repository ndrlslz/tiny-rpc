package com.ndrlslz.tiny.rpc.core.serialization;

public interface Serializer {
    <T> byte[] serialize(T object);

    <T> T deserialize(byte[] bytes, Class<T> clazz);
}