package com.ndrlslz.tiny.rpc.core.protocol;

import java.io.Serializable;

public class TinyRpcRequest extends TinyRpcMessage implements Serializable {
    private String methodName;
    private Object[] argumentsValue;

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgumentsValue() {
        return argumentsValue;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setArgumentsValue(Object[] argumentsValue) {
        this.argumentsValue = argumentsValue;
    }
}
