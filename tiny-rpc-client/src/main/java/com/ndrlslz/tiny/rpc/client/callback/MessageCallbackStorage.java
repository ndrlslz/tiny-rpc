package com.ndrlslz.tiny.rpc.client.callback;

import java.util.concurrent.ConcurrentHashMap;

public class MessageCallbackStorage {
    private static ConcurrentHashMap<String, MessageCallback> messageCallbacks;

    static {
        messageCallbacks = new ConcurrentHashMap<>();
    }

    public static void put(String id, MessageCallback messageCallback) {
        messageCallbacks.put(id, messageCallback);
    }

    public static MessageCallback get(String id) {
        return messageCallbacks.get(id);
    }

    public static void remove(String id) {
        messageCallbacks.remove(id);
    }
}