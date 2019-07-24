package com.ndrlslz.tiny.rpc.client.callback;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MessageCallback {
    private Object result;
    private ReentrantLock lock;
    private Condition MessageCallbackCondition;

    public MessageCallback() {
        lock = new ReentrantLock();
        MessageCallbackCondition = lock.newCondition();
    }

    public Object get(Integer timeout) throws InterruptedException {
        lock.lock();
        MessageCallbackCondition.await(timeout, TimeUnit.SECONDS);
        lock.unlock();

        return result;
    }

    public void done(Object object) {
        this.result = object;

        lock.lock();
        MessageCallbackCondition.signal();
        lock.unlock();
    }
}