package com.ndrlslz.tiny.rpc.client.callback;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MessageCallback {
    private Object result;
    private ReentrantLock lock;
    private Condition messageCallbackCondition;

    public MessageCallback() {
        lock = new ReentrantLock();
        messageCallbackCondition = lock.newCondition();
    }

    public Object get(Integer timeout) throws InterruptedException {
        try {
            lock.lock();
            messageCallbackCondition.await(timeout, TimeUnit.SECONDS);

            return result;
        } finally {
            lock.unlock();
        }
    }

    public void done(Object object) {
        try {
            lock.lock();
            messageCallbackCondition.signalAll();
            this.result = object;
        } finally {
            lock.unlock();
        }
    }
}