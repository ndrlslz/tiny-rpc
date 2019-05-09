package com.ndrlslz.tiny.rpc.server.protocol;

import java.io.Serializable;

public class TinyRpcResponse implements Serializable {
    private String correlationId;
    private String methodName;
    private Class responseType;
    private Object responseValue;

    public String getCorrelationId() {
        return correlationId;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class getResponseType() {
        return responseType;
    }

    public Object getResponseValue() {
        return responseValue;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setResponseType(Class responseType) {
        this.responseType = responseType;
    }

    public void setResponseValue(Object responseValue) {
        this.responseValue = responseValue;
    }
}
