package com.ndrlslz.tiny.rpc.server.protocol;

import java.io.Serializable;

public class TinyRpcRequest implements Serializable {
    private String correlationId;
    private String methodName;
    private Object[] argumentsValue;

    public String getCorrelationId() {
        return correlationId;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgumentsValue() {
        return argumentsValue;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setArgumentsValue(Object[] argumentsValue) {
        this.argumentsValue = argumentsValue;
    }
}
