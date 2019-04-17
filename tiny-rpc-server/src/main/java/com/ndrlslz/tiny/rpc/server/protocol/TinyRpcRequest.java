package com.ndrlslz.tiny.rpc.server.protocol;

import java.io.Serializable;

public class TinyRpcRequest implements Serializable {
    private String methodName;
    private int argumentsCount;
    private Object[] argumentsValue;

    public String getMethodName() {
        return methodName;
    }

    public int getArgumentsCount() {
        return argumentsCount;
    }

    public Object[] getArgumentsValue() {
        return argumentsValue;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setArgumentsCount(int argumentsCount) {
        this.argumentsCount = argumentsCount;
    }

    public void setArgumentsValue(Object[] argumentsValue) {
        this.argumentsValue = argumentsValue;
    }
}
